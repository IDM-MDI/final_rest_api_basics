package com.epam.esm.generator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private static final String uriUser = "https://randomuser.me/api";
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftRepository;

    @Autowired
    public DataGenerator(TagRepository tagRepository, GiftCertificateRepository giftRepository) {
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
    }


    public void fillRandomData() {
        if(tagRepository.count() == 0) {
            String[] words = getWords();
            initTags(words);
            if(giftRepository.count() == 0)
                initGifts(words);
        }
        if(giftRepository.count() == 0) {
            initUsers(getUsernames());
        }
    }

    private void initUsers(List<String> usernames) {
    }

    private void initGifts(String[] words) {
        long minDuration = 1, maxDuration = 100;
        long minPrice = 1000, maxPrice = 100000000;
        List<GiftCertificate> gifts = new ArrayList<>();
        List<String> giftWords = getCountWords(words,10000).stream().toList();
        for (int i = 0; i < 10000; i++) {
            GiftCertificate gift = new GiftCertificate();
            gift.setName(giftWords.get(i));
            gift.setTagList(getRandomTags());
            gift.setDuration((int) getRandomNumber(minDuration,maxDuration));
            gift.setPrice(new BigDecimal(getRandomNumber(minPrice,maxPrice)));
            gift.setDescription(getRandomDescription(words));
            gifts.add(gift);
        }
        giftRepository.saveAll(gifts);
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
    private List<String> getUsernames() {
        String results = "results", name = "name", first = "first", last = "last";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriUser))
                .build();
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String user = response.body();

            JsonObject userObject = JsonParser.parseString(user)
                    .getAsJsonObject()
                    .get(results)
                    .getAsJsonArray().get(0)
                    .getAsJsonObject()
                    .get(name)
                    .getAsJsonObject();
            String firstName = userObject.get(first).getAsJsonPrimitive().getAsString();
            String lastName = userObject.get(last).getAsJsonPrimitive().getAsString();
            usernames.add(firstName + lastName);
        }
//        JsonObject jo = userObject.getAsJsonObject();
//        JsonElement results = jo.get("results");
//        JsonElement resultJO = results.getAsJsonArray().get(0);
//        JsonObject name = resultJO.getAsJsonObject();
//        JsonElement nameElement = name.get("name");
//        String fands = nameElement.getAsJsonObject().get("first").getAsJsonPrimitive().getAsString();
//// Достаём firstName and lastName
//        System.out.println(fands);
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

    private void initTags(String[] words) {
        List<Tag> tags = new ArrayList<>();
        List<String> tagsWord = getCountWords(words,1000).stream().toList();
        for (int i = 0; i < 1000; i++) {
            Tag tag = new Tag();
            tag.setName(tagsWord.get(i));
            tags.add(tag);
        }
        tagRepository.saveAll(tags);
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
}
