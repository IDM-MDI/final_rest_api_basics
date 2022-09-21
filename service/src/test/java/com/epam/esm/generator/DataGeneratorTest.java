package com.epam.esm.generator;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.impl.RoleModelMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataGeneratorTest {
    private static final String URI_OF_USER_API = "https://random-data-api.com/api/v2/users";
    private HttpResponse<String> response = new HttpResponse<String>() {
        @Override
        public int statusCode() {
            return 0;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return null;
        }

        @Override
        public String body() {
            return "testBody";
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return null;
        }
    };
    private final InputStream stubInputStream = IOUtils.toInputStream(
            "{" +
                   "username: testUsername,\n" +
                   "password: testPass" +
                  "}", "UTF-8");
    @Mock
    private TagRepository tagRepository = mock(TagRepository.class);
    @Mock
    private GiftCertificateRepository giftRepository = mock(GiftCertificateRepository.class);
    @Mock
    private UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
    @Mock
    private OrderRepository orderRepository = mock(OrderRepository.class);
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);
    @Mock
    private RoleModelMapper roleMapperMock = mock(RoleModelMapper.class);
    @Mock
    private RoleRepository roleRepositoryMock = mock(RoleRepository.class);

    @InjectMocks
    private DataGenerator generator;
    @SneakyThrows
    @Test
    void fillRandomData() {
        HttpClient client = mock(HttpClient.class);

        Role admin = new Role(1L,"ADMIN");
        RoleDto adminDto = new RoleDto(1L,"ADMIN");

        Set<String> words = new HashSet<>(List.of("word1","word2","word3"));

        Tag tag = new Tag();
        GiftCertificate gift = new GiftCertificate(
                1L,
                "",
                "",
                new BigDecimal("0"),
                null,null,null,null,null,null,null,null,null
        );
        User emptyUser = new User();

        try(MockedStatic<HttpClient> clientMocked = mockStatic(HttpClient.class);
            MockedStatic<RandomHandler> randomHandler = mockStatic(RandomHandler.class);
            MockedConstruction<FileWriter> mockedWriter = mockConstruction(FileWriter.class, (writer, context) -> {
                doNothing().when(writer).write(anyString());
            })) {

            when(HttpClient.newHttpClient())
                    .thenReturn(client);
            when(client.send(any(HttpRequest.class),eq(HttpResponse.BodyHandlers.ofString())))
                    .thenReturn(response);

            doReturn(990L).when(userRepository).count();
            doReturn(890L).when(tagRepository).count();
            doReturn(8999L).when(giftRepository).count();
            doReturn(Optional.of(admin)).when(roleRepositoryMock).findRoleByName("ADMIN");
            doReturn(Optional.of(admin)).when(roleRepositoryMock).findRoleByName("USER");
            doReturn(adminDto).when(roleMapperMock).toDto(admin);

            when(RandomHandler.getCountWords(any(),anyInt()))
                    .thenReturn(words);
            when(RandomHandler.getRandomNumber(0L,3L)).thenReturn(1L);

            doReturn(emptyUser).when(userServiceImpl).save(any());
            doReturn(tag).when(tagRepository).save(any());
            doReturn(gift).when(giftRepository).save(any());

            doReturn(List.of(emptyUser)).when(userRepository).findUsersByOrdersEmpty();

            generator.fillRandomData();

            verify(userRepository,atLeast(1)).count();
            verify(tagRepository,atLeast(1)).count();
            verify(giftRepository,atLeast(1)).count();
        }
    }
    @SneakyThrows
    @Test
    void fillRandomDataAnEmpty() {
        HttpClient client = mock(HttpClient.class);

        try(MockedStatic<HttpClient> clientMocked = mockStatic(HttpClient.class)) {
            when(HttpClient.newHttpClient())
                    .thenReturn(client);
            when(client.send(any(HttpRequest.class),eq(HttpResponse.BodyHandlers.ofString())))
                    .thenReturn(response);

            doReturn(1000L).when(userRepository).count();
            doReturn(1000L).when(tagRepository).count();
            doReturn(10000L).when(giftRepository).count();

            doReturn(List.of()).when(userRepository).findUsersByOrdersEmpty();

            generator.fillRandomData();

            verify(userRepository,atLeast(1)).count();
            verify(tagRepository,atLeast(1)).count();
            verify(giftRepository,atLeast(1)).count();
        }
    }
}