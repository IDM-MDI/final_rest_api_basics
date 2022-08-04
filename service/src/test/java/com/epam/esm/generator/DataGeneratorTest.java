package com.epam.esm.generator;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

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
    private UserServiceImpl userServiceImpl = Mockito.mock(UserServiceImpl.class);
    @Mock
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);


    @InjectMocks
    private DataGenerator generator;

    @SneakyThrows
    @Test
    void fillRandomData() {
        Set<String> words = Set.of("tag","gift","five","press");

        when(tagRepository.count()).thenReturn(0L);
        doReturn(0L).doReturn(5L).when(giftRepository).count();
        doReturn(0L).doReturn(5L).when(userRepository).count();
        when(orderRepository.count()).thenReturn(0L);

        try(MockedStatic<RandomHandler> randomHandler = mockStatic(RandomHandler.class)) {
            when(RandomHandler.getCountWords(any(String[].class),eq(1000))).thenReturn(words);
            when(RandomHandler.getCountWords(any(String[].class),eq(10000))).thenReturn(words);

            generator.fillRandomData();
        }

        verify(tagRepository,atLeast(1)).count();
        verify(giftRepository,atLeast(1)).count();
        verify(userRepository,atLeast(1)).count();
        verify(orderRepository).count();
    }
}