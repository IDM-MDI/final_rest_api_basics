package com.epam.esm.generator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
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
    private static final String uri = "http://www-personal.umich.edu/~jlawler/wordlist";
    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftRepository;

    @Autowired
    public DataGenerator(TagRepository tagRepository, GiftCertificateRepository giftRepository) {
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
    }


    public void fillRandomData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        String[] words = getWords(client, request);
        if(tagRepository.count() == 0)
            initTags(words);
        if(giftRepository.count() == 0)
            initGifts(words);
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
    private String[] getWords(HttpClient client, HttpRequest request) {
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        String word = response.body();
        return word.split("\r\n");
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
