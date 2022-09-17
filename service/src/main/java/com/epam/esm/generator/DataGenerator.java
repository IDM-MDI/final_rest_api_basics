package com.epam.esm.generator;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.OrderBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.impl.RoleModelMapper;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.epam.esm.entity.StatusName.ACTIVE;

@Component
@Slf4j
public class DataGenerator {
    private static final String URI_OF_VOCABULARY = "http://www-personal.umich.edu/~jlawler/wordlist";
    private static final String URI_OF_ADDRESS_API = "https://random-data-api.com/api/v2/addresses";
    private static final String URI_OF_USER_API = "https://random-data-api.com/api/v2/users";
    private static final String URI_OF_RANDOM_IMG = "https://picsum.photos/800/600";
    private static final OrderBuilder ORDER_BUILDER = new OrderBuilder();
    private static final GiftCertificateBuilder GIFT_CERTIFICATE_BUILDER = new GiftCertificateBuilder();
    private static final TagBuilder TAG_BUILDER = new TagBuilder();
    private final TagRepository tagRepository;
    private final RoleRepository roleRepository;
    private final GiftCertificateRepository giftRepository;
    private final UserRepository userRepository;
    private final RoleModelMapper roleMapper;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public DataGenerator(TagRepository tagRepository,
                         RoleRepository roleRepository,
                         GiftCertificateRepository giftRepository,
                         UserRepository userRepository,
                         UserServiceImpl userServiceImpl,
                         RoleModelMapper roleMapper) {
        this.tagRepository = tagRepository;
        this.roleRepository = roleRepository;
        this.giftRepository = giftRepository;
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }


    public void fillRandomData() {
        String[] words = getWords();

        initUsers(getFilteredUsers());
        initTags(words);
        initGifts(words);
        initOrders();

        log.info("All data has successfully generated");
    }

    private void initOrders() {
        List<User> users = userRepository.findUsersByOrdersEmpty();
        if(users.isEmpty()) {
            return;
        }

        log.info("Starting generate orders for user");
        users.forEach(user -> {
            List<Order> orders = new ArrayList<>();
            List<GiftCertificate> gifts = getRandomGifts();
            gifts.forEach(i-> {
                Order order = ORDER_BUILDER
                                    .setPrice(i.getPrice())
                                    .setUser(user)
                                    .setGift(i)
                                    .setStatus(ACTIVE.name())
                                    .build();
                orders.add(order);
            });
            user.setOrders(orders);
        });
        userRepository.saveAll(users);
    }

    @SneakyThrows
    private void initUsers(List<UserDto> users) {
        if(users.isEmpty()) {
            return;
        }

        ExecutorService service = Executors.newFixedThreadPool(5);
        for (UserDto user : users) {
            service.execute(()-> {
                try {
                    userServiceImpl.save(user);
                } catch (RepositoryException e) {
                    throw new RuntimeException("ERROR WHILE CREATING RANDOM USERS");
                }
            });
        }
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        log.info("all users saved");
    }

    @SneakyThrows
    private void initGifts(String[] words) {
        log.info("Starting generate random gifts");

        long wordCount = 10000;
        long giftCount = giftRepository.count();
        long count = wordCount - giftCount;

        long minDuration = 1;
        long maxDuration = 100;
        long minPrice = 1000;
        long maxPrice = 100000000;

        if(count <= 1000) {
            return;
        }

        ExecutorService service = Executors.newFixedThreadPool(5);
        List<String> giftWords = Collections.synchronizedList(RandomHandler.getCountWords(words, (int) count)
                .stream()
                .toList());
        for (String i : giftWords) {
            service.execute(() -> {
                try {
                    GiftCertificate gift = GIFT_CERTIFICATE_BUILDER
                            .setName(i)
                            .setPrice(new BigDecimal(RandomHandler.getRandomNumber(minPrice, maxPrice)))
                            .setDuration((int) RandomHandler.getRandomNumber(minDuration, maxDuration))
                            .setDescription(getRandomDescription(words))
                            .setShop(getRandomAddress())
                            .setMainImage(getRandomImage())
                            .setSecondImage(getRandomImage())
                            .setThirdImage(getRandomImage())
                            .setStatus(ACTIVE.name())
                            .setTagList(getRandomTags())
                            .build();
                    giftRepository.save(gift);
                } catch (DataIntegrityViolationException e) {
                    System.out.println(e.getRootCause().getMessage());
                }
            });
        }
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    @SneakyThrows
    private void initTags(String[] words) {
        long wordCount = 1000;
        long tagCount = tagRepository.count();
        long count = wordCount - tagCount;
        if(count <= 100) {
            return;
        }

        log.info("Starting generate random tags");
        List<String> tagsWord = Collections.synchronizedList(RandomHandler
                .getCountWords(words, (int) count)
                .stream()
                .toList());
        ExecutorService service = Executors.newFixedThreadPool(5);
        tagsWord.forEach(i -> {
            service.execute(() -> {
                try {
                    Tag tag = TAG_BUILDER
                            .setName(i)
                            .setStatus(ACTIVE.name())
                            .setMainImage(getRandomImage())
                            .build();
                    tagRepository.save(tag);
                } catch (DataIntegrityViolationException e) {
                    System.out.println(e.getMessage());
                }
            });
        });
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
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
    private List<UserDto> getFilteredUsers() {
        long userCount = userRepository.count();
        long wordCount = 1000;
        if(wordCount - userCount < 10) {
            return new ArrayList<>();
        }
        log.info("Starting generate random users");
        List<UserDto> randomUsers = getRandomUsers();
        writeUsersPrincipalToFile(randomUsers);
        return randomUsers;
    }

    private String getRandomDescription(String[] words) {
        StringBuilder builder = new StringBuilder();
        long min = 20;
        long max = 35;
        long random = RandomHandler.getRandomNumber(min,max);
        for (int i = 0; i < random; i++) {
            builder.append(RandomHandler.getRandomWord(words))
                    .append(" ");
        }
        return builder.toString().trim();
    }

    private List<Tag> getRandomTags() {
        long min = 0;
        long max = 3;
        List<Tag> tags = new ArrayList<>();
        long randomCount = RandomHandler.getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            tagRepository.findRandomTag(PageRequest.of(0,1))
                    .stream()
                    .findFirst()
                    .ifPresent(tags::add);
        }
        return tags;
    }

    private List<GiftCertificate> getRandomGifts() {
        long min = 0;
        long max = 5;
        long maxGifts = giftRepository.count();
        List<GiftCertificate> gifts = new ArrayList<>();
        long randomCount = RandomHandler.getRandomNumber(min,max);
        for (int j = 0; j < randomCount; j++) {
            long randomNumber = RandomHandler.getRandomNumber(min, maxGifts);
            Optional<GiftCertificate> optionalGift = giftRepository.findById(randomNumber);
            optionalGift.ifPresent(gifts::add);
        }
        return gifts;
    }
    private byte[] getRandomImage() {
        try {
            URL url = new URL(URI_OF_RANDOM_IMG);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                byte[] chunk = new byte[4096];
                int bytesRead;
                InputStream stream = url.openStream();

                while ((bytesRead = stream.read(chunk)) > 0) {
                    outputStream.write(chunk, 0, bytesRead);
                }
            } catch (IOException e) {
                return null;
            }
            return outputStream.toByteArray();
        } catch (IOException ex) {
            return null;
        }
    }
    private String getRandomAddress() {
        JsonObject json = UrlJsonParser.readJsonFromUrl(URI_OF_ADDRESS_API);
        return UrlJsonParser.readStringFromJson(json, "full_address");
    }
    @SneakyThrows
    private List<UserDto> getRandomUsers() {
        long tagCount = tagRepository.count();
        List<UserDto> result = new ArrayList<>();

        RoleDto admin = roleMapper.toDto(roleRepository.findRoleByName("ADMIN")
                .orElseThrow());
        RoleDto user = roleMapper.toDto(roleRepository.findRoleByName("USER")
                .orElseThrow());
        List<RoleDto> roleListAdmin = List.of(admin, user);
        List<RoleDto> roleListUser = List.of(user);

        ExecutorService service = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1000 - tagCount; i++) {
            int j = i;
            service.execute(()-> {
                UserDto randomUser = getRandomUser();
                randomUser.setRoles(j % 100 == 0 ? roleListAdmin : roleListUser);
                result.add(randomUser);
            });
        }
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        return result;
    }

    @SneakyThrows
    private void writeUsersPrincipalToFile(List<UserDto> users) {
        String path = "D:\\Project\\users.txt";
        try(FileWriter myWriter = new FileWriter(path)) {
            for (UserDto user : users) {
                myWriter.write("username : " + user.getUsername() +
                        " password: " + user.getPassword() + " ADMIN: " + (user.getRoles().size() == 2?"YES": "NO") + "\n");
            }
        }
    }
    private UserDto getRandomUser() {
        JsonObject json = UrlJsonParser.readJsonFromUrl(URI_OF_USER_API);
        UserDto result = new UserDto();
        result.setUsername(UrlJsonParser.readStringFromJson(json,"username"));
        result.setPassword(UrlJsonParser.readStringFromJson(json,"password"));
        return result;
    }
}
