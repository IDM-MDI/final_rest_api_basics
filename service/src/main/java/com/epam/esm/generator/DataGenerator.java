package com.epam.esm.generator;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.entity.*;
import com.epam.esm.repository.*;
import com.epam.esm.util.HashGenerator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Component
public class DataGenerator {
    private static final String uriWord = "http://www-personal.umich.edu/~jlawler/wordlist";
    private final RandomHandler handler;
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public DataGenerator(RandomHandler handler,
                         TagRepository tagRepository,
                         GiftCertificateRepository giftRepository,
                         UserRepository userRepository,
                         OrderRepository orderRepository, StatusRepository statusRepository) {
        this.handler = handler;
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
    }

    public void fillRandomData() {
        long tagCount = tagRepository.count();
        long giftCount = giftRepository.count();
        long userCount = userRepository.count();
        long orderCount = orderRepository.count();
        String[] words = null;
        if(tagCount == 0 || giftCount == 0 || userCount == 0) {
            words = getWords();
        }
        if(tagCount == 0) {
            initTags(words);
        }
        if(giftCount == 0) {
            initGifts(words);
        }
        if(userCount == 0) {
            initUsers(getRandomUsers(words));
        }
        userCount = userRepository.count();
        if(orderCount == 0 &&
                userCount != 0 &&
                giftCount != 0) {
            initOrders();
        }
    }

    private void initOrders() {
        List<User> users = userRepository.findUsersByOrdersEmpty();
        Optional<Status> activeStatus = statusRepository.findById(1L);

        users.forEach(user -> {
            List<Order> orders = new ArrayList<>();
            List<GiftCertificate> gifts = getRandomGifts();
            gifts.forEach(i-> {
                Order order = new OrderBuilder()
                                    .setPrice(i.getPrice())
                                    .setUser(user)
                                    .setGift(i)
                                    .build();
                activeStatus.ifPresent(order::setStatus);
                orders.add(order);
            });
            user.setOrders(orders);
        });
        userRepository.saveAll(users);
    }

    private void initUsers(List<User> users) {
        userRepository.saveAll(users);
    }

    private void initGifts(String[] words) {
        long minDuration = 1, maxDuration = 100;
        long minPrice = 1000, maxPrice = 100000000;
        List<GiftCertificate> gifts = new ArrayList<>();
        List<String> giftWords = handler.getCountWords(words,10000).stream().toList();
        Optional<Status> activeStatus = statusRepository.findById(1L);

        for (String giftWord : giftWords) {
            GiftCertificate gift = new GiftCertificateBuilder()
                                        .setName(giftWord)
                                        .setPrice(new BigDecimal(handler.getRandomNumber(minPrice, maxPrice)))
                                        .setDuration((int) handler.getRandomNumber(minDuration, maxDuration))
                                        .setDescription(getRandomDescription(words))
                                        .setTagList(getRandomTags())
                                        .build();
            activeStatus.ifPresent(gift::setStatus);
            gifts.add(gift);
        }
        giftRepository.saveAll(gifts);
    }

    private void initTags(String[] words) {
        Optional<Status> activeStatus = statusRepository.findById(1L);
        List<Tag> tags = new ArrayList<>();
        List<String> tagsWord = handler
                                .getCountWords(words,1000)
                                .stream()
                                .toList();
        tagsWord.forEach(i -> {
            Tag tag = new TagBuilder().setName(i).build();
            activeStatus.ifPresent(tag::setStatus);
            tags.add(tag);
        });
        tagRepository.saveAll(tags);
    }
    @SneakyThrows
    private String[] getWords() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriWord))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        String word = response.body();
        return word.split("\r\n");
    }
    @SneakyThrows
    private List<User> getRandomUsers(String[] words) {
        Optional<Status> activeStatus = statusRepository.findById(1L);
        String mr = "Mr ";
        String mrs = "Mrs ";
        List<User> users = new ArrayList<>();
        List<String> exampleUsernames = handler
                                        .getCountWords(words,1000)
                                        .stream()
                                        .toList();

        for (String i : exampleUsernames) {
            User user = new User();
            if (handler.getRandomNumber(0, 1) == 0) {
                user.setUsername(mr + i);
            }
            else {
                user.setUsername(mrs + i);
            }
            user.setPassword(HashGenerator.generatePassword(i));
            activeStatus.ifPresent(user::setStatus);
            users.add(user);
        }
        return users;
    }


    private String getRandomDescription(String[] words) {
        StringBuilder builder = new StringBuilder();
        long min = 20;
        long max = 35;
        long random = handler.getRandomNumber(min,max);
        for (int i = 0; i < random; i++) {
            builder.append(handler.getRandomWord(words))
                    .append(" ");
        }
        return builder.toString().trim();
    }

    private List<Tag> getRandomTags() {
        long min = 0;
        long max = 3;
        long maxTags = tagRepository.count();
        List<Tag> tags = new ArrayList<>();
        long randomCount = handler.getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            long randomNumber = handler.getRandomNumber(min,maxTags);
            Optional<Tag> tagOptional = tagRepository.findById(randomNumber);
            tagOptional.ifPresent(tags::add);
        }
        return tags;
    }
    private List<GiftCertificate> getRandomGifts() {
        long min = 0;
        long max = 5;
        long maxGifts = giftRepository.count();
        List<GiftCertificate> gifts = new ArrayList<>();
        long randomCount = handler.getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            long randomNumber = handler.getRandomNumber(min, maxGifts);
            Optional<GiftCertificate> optionalGift = giftRepository.findById(randomNumber);
            optionalGift.ifPresent(gifts::add);
        }
        return gifts;
    }

}
