package com.epam.esm.service;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.exception.RepositoryExceptionCode.REPOSITORY_NOTHING_FIND_BY_ID;

@Service
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
public class UserService {

    private final UserRepository repository;
    private final UserModelMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DtoPage<UserDto> findAll(Integer page, Integer size, String sort) {
        List<User> userList = repository.findAll(PageRequest.of(page,size, Sort.by(sort))).toList();
        DtoPage<UserDto> dtoPage = new DtoPage<>();
        dtoPage.setContent(mapper.toDtoList(userList));
        dtoPage.setNumberOfPage(page);
        dtoPage.setSize(size);
        dtoPage.setSortBy(sort);
        return dtoPage;
    }

    public DtoPage<UserDto> findById(Long id) throws RepositoryException {
        DtoPage<UserDto> dtoPage = new DtoPage<>();
        Optional<User> byId = repository.findById(id);
        if(byId.isEmpty())
            throw new RepositoryException(REPOSITORY_NOTHING_FIND_BY_ID.toString());

        dtoPage.setContent(List.of(mapper.toDto(byId.get())));
        return dtoPage;
    }
}
