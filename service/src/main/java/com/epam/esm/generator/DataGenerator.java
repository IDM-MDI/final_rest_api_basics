package com.epam.esm.generator;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.entity.StatusName.ACTIVE;

@Component
@Slf4j
public class DataGenerator {
    private static final String URI_OF_VOCABULARY = "http://www-personal.umich.edu/~jlawler/wordlist";
    private static final OrderBuilder ORDER_BUILDER = new OrderBuilder();
    private static final GiftCertificateBuilder GIFT_CERTIFICATE_BUILDER = new GiftCertificateBuilder();
    private static final TagBuilder TAG_BUILDER = new TagBuilder();
    private final RandomHandler handler;
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataGenerator(RandomHandler handler,
                         TagRepository tagRepository,
                         GiftCertificateRepository giftRepository,
                         UserRepository userRepository,
                         OrderRepository orderRepository, StatusRepository statusRepository, UserService userService) {
        this.handler = handler;
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    public void fillRandomData() throws RepositoryException {
        long tagCount = tagRepository.count();
        long giftCount = giftRepository.count();
        long userCount = userRepository.count();
        long orderCount = orderRepository.count();
        String[] words = null;
        if(tagCount == 0 || giftCount == 0 || userCount == 0) {
            words = getWords();
        }
        if(tagCount == 0) {
            log.info("Starting generate random tags");
            initTags(words);
        }
        if(giftCount == 0) {
            log.info("Starting generate random gifts");
            initGifts(words);
        }
        if(userCount == 0) {
            log.info("Starting generate random users");
            initUsers(getRandomUsers(words));
        }
        userCount = userRepository.count();
        if(orderCount == 0 &&
                userCount != 0 &&
                giftCount != 0) {
            log.info("Starting generate orders for user");
            initOrders();
        }
        log.info("All data has successfully generated");
    }

    private void initOrders() {
        List<User> users = userRepository.findUsersByOrdersEmpty();
        Optional<Status> activeStatus = statusRepository.findByNameIgnoreCase(ACTIVE.name());

        users.forEach(user -> {
            List<Order> orders = new ArrayList<>();
            List<GiftCertificate> gifts = getRandomGifts();
            gifts.forEach(i-> {
                Order order = ORDER_BUILDER
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

    private void initUsers(List<UserDto> users) throws RepositoryException {
        for (int i = 0; i < users.size(); i++) {
            userService.save(users.get(i));
            log.info("User[{}]: is saved", i);
        }
    }

    private void initGifts(String[] words) {
        long minDuration = 1, maxDuration = 100;
        long minPrice = 1000, maxPrice = 100000000;
        List<GiftCertificate> gifts = new ArrayList<>();
        List<String> giftWords = handler.getCountWords(words,10000).stream().toList();
        Optional<Status> activeStatus = statusRepository.findByNameIgnoreCase(ACTIVE.name());

        for (String giftWord : giftWords) {
            GiftCertificate gift = GIFT_CERTIFICATE_BUILDER
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
        Optional<Status> activeStatus = statusRepository.findByNameIgnoreCase(ACTIVE.name());
        List<Tag> tags = new ArrayList<>();
        List<String> tagsWord = handler
                                .getCountWords(words,1000)
                                .stream()
                                .toList();
        tagsWord.forEach(i -> {
            Tag tag = TAG_BUILDER
                    .setName(i)
                    .build();
            activeStatus.ifPresent(tag::setStatus);
            tags.add(tag);
        });
        tagRepository.saveAll(tags);
    }
    @SneakyThrows
    private String[] getWords() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_OF_VOCABULARY))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        String word = response.body();
        return word.split("\r\n");
    }
    @SneakyThrows
    private List<UserDto> getRandomUsers(String[] words) {
        String mr = "Mr ";
        String mrs = "Mrs ";
        List<UserDto> users = new ArrayList<>();
        List<String> exampleUsernames = handler
                                        .getCountWords(words,1000)
                                        .stream()
                                        .toList();

        for (String i : exampleUsernames) {
            UserDto user = new UserDto();
            if (handler.getRandomNumber(0, 1) == 0) {
                user.setUsername(mr + i);
            }
            else {
                user.setUsername(mrs + i);
            }
            user.setPassword(i);
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
