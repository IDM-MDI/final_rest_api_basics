package com.epam.esm.generator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Component
public class DataGenerator {
    private static final String uriWord = "http://www-personal.umich.edu/~jlawler/wordlist";
    private static final String uriUser = "https://randomuser.me/api";
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataGenerator(TagRepository tagRepository, GiftCertificateRepository giftRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void fillRandomData() {
        long tagCount = tagRepository.count();
        long giftCount = giftRepository.count();
        long userCount = userRepository.count();
        String[] words = null;
        if(tagCount == 0 || giftCount == 0 || userCount == 0)
            words = getWords();

        if(tagCount == 0)
            initTags(words);

        if(giftCount == 0)
            initGifts(words);

        if(userCount == 0)
            initUsers(getUsernames(words));
    }

    private void initUsers(List<String> usernames) {
        List<User> users = new ArrayList<>();
        for (String username : usernames) {
            User user = new User();
            List<GiftCertificate> gifts = getRandomGifts();
            user.setName(username);
            user.setOrders(gifts);
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    private void initGifts(String[] words) {
        long minDuration = 1, maxDuration = 100;
        long minPrice = 1000, maxPrice = 100000000;
        List<GiftCertificate> gifts = new ArrayList<>();
        List<String> giftWords = getCountWords(words,10000).stream().toList();
        for (String giftWord : giftWords) {
            GiftCertificate gift = new GiftCertificate();
            gift.setName(giftWord);
            gift.setTagList(getRandomTags());
            gift.setDuration((int) getRandomNumber(minDuration, maxDuration));
            gift.setPrice(new BigDecimal(getRandomNumber(minPrice, maxPrice)));
            gift.setDescription(getRandomDescription(words));
            gifts.add(gift);
        }
        giftRepository.saveAll(gifts);
    }

    private void initTags(String[] words) {
        List<Tag> tags = new ArrayList<>();
        List<String> tagsWord = getCountWords(words,1000).stream().toList();
        for (String s : tagsWord) {
            Tag tag = new Tag();
            tag.setName(s);
            tags.add(tag);
        }
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
    private List<String> getUsernames(String[] words) {
        String mr = "Mr ", mrs = "Mrs ";
        List<String> usernames = new ArrayList<>();
        List<String> exampleUsernames = getCountWords(words,1000).stream().toList();
        for (String exampleUsername : exampleUsernames) {
            String user;
            if (getRandomNumber(0, 1) == 0)
                user = mr + exampleUsername;
            else
                user = mrs + exampleUsername;
            usernames.add(user);
        }
        return usernames;
    }

    private Set<String> getCountWords(String[] words,int count) {
        long min = 0, max = 68000;
        Set<String> setWords = new HashSet<>();
        for (int i = 0; i < count; i++) {
            if(setWords.size() == i)
                setWords.add(words[(int) getRandomNumber(min,max)]);
            else
                i = setWords.size()-1;
        }
        return setWords;
    }

    private long getRandomNumber(long min,long max) {
        Random random = new Random();
        return random.nextLong((max - min) + 1) + min;
    }
    private String getRandomWord(String[] words) {
        Random random = new Random();
        int min = 0,max = 68000;
        return words[random.nextInt((max - min) + 1) + min];
    }

    private String getRandomDescription(String[] words) {
        StringBuilder builder = new StringBuilder();
        long min = 20,max = 35;
        long random = getRandomNumber(min,max);
        for (int i = 0; i < random; i++) {
            builder.append(getRandomWord(words))
                    .append(" ");
        }
        return builder.toString().trim();
    }

    private List<Tag> getRandomTags() {
        long min = 0, max = 3;
        long maxTags = tagRepository.count();
        List<Tag> tags = new ArrayList<>();
        long randomCount = getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            long randomNumber = getRandomNumber(min,maxTags);
            Optional<Tag> tagOptional = tagRepository.findById(randomNumber);
            tagOptional.ifPresent(tags::add);
        }
        return tags;
    }
    private List<GiftCertificate> getRandomGifts() {
        long min = 0, max = 5;
        long maxGifts = giftRepository.count();
        List<GiftCertificate> gifts = new ArrayList<>();
        long randomCount = getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            long randomNumber = getRandomNumber(min, maxGifts);
            Optional<GiftCertificate> optionalGift = giftRepository.findById(randomNumber);
            optionalGift.ifPresent(gifts::add);
        }
        return gifts;
    }

    private boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
}
