package com.epam.esm.generator;

import com.epam.esm.entity.Status;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.StatusRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataGeneratorTest {

    @Mock
    private TagRepository tagRepository = Mockito.mock(TagRepository.class);
    @Mock
    private GiftCertificateRepository giftRepository = Mockito.mock(GiftCertificateRepository.class);
    @Mock
    private UserService userService = Mockito.mock(UserService.class);
    @Mock
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    @Mock
    private StatusRepository statusRepository = Mockito.mock(StatusRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);


    @InjectMocks
    private DataGenerator generator;

    @SneakyThrows
    @Test
    void fillRandomData() {
        MockedStatic<RandomHandler> randomHandler = mockStatic(RandomHandler.class);
        Optional<Status> optionalStatus = Optional.of(new Status(1L,"ACTIVE"));
        Set<String> words = Set.of("tag","gift","five","press");

        when(tagRepository.count()).thenReturn(0L);
        doReturn(0L).doReturn(5L).when(giftRepository).count();
        doReturn(0L).doReturn(5L).when(userRepository).count();
        when(orderRepository.count()).thenReturn(0L);
        when(statusRepository.findByNameIgnoreCase(ACTIVE.name())).thenReturn(optionalStatus);
        randomHandler.when(()-> RandomHandler.getCountWords(any(String[].class),eq(1000))).thenReturn(words);
        randomHandler.when(()-> RandomHandler.getCountWords(any(String[].class),eq(10000))).thenReturn(words);

        generator.fillRandomData();

        verify(tagRepository,atLeast(1)).count();
        verify(giftRepository,atLeast(1)).count();
        verify(userRepository,atLeast(1)).count();
        verify(orderRepository).count();
        verify(statusRepository).findByNameIgnoreCase(ACTIVE.name());
    }
}