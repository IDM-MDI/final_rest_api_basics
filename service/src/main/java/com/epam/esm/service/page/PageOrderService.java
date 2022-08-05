package com.epam.esm.service.page;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.PageService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.dto.ResponseTemplate.*;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.service.impl.OrderServiceImpl.ORDER;

@Service
public class PageOrderService implements PageService<DtoPage<OrderDto>,OrderDto> {
    private final OrderServiceImpl service;
    private final UserServiceImpl userServiceImpl;
    private final GiftCertificateModelMapper giftMapper;
    private final ResponseService responseService;

    @Autowired
    public PageOrderService(OrderServiceImpl service,
                            UserServiceImpl userServiceImpl,
                            GiftCertificateModelMapper giftMapper,
                            ResponseService responseService) {
        this.service = service;
        this.userServiceImpl = userServiceImpl;
        this.giftMapper = giftMapper;
        this.responseService = responseService;
    }

    public DtoPage<OrderDto> save(String username, long id) throws RepositoryException {
        UserDto userByUsername = userServiceImpl.findUserByUsername(username);
        OrderDto dto = new OrderDto();
        dto.setGiftId(id);
        dto.setUserId(userByUsername.getId());
        return save(dto);
    }

    @Override
    public DtoPage<OrderDto> save(OrderDto dto) throws RepositoryException {
        return new DtoPageBuilder<OrderDto>()
                .setContent(List.of(mapper(service.save(dto))))
                .setResponse(responseService.createdResponse(ORDER + CREATED))
                .build();
    }

    @Override
    public DtoPage<OrderDto> update(OrderDto dto, long id) throws RepositoryException {
        dto.setId(id);
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.createdResponse(ORDER + UPDATED))
                .setContent(List.of(mapper(service.update(dto))))
                .build();
    }

    @Override
    public DtoPage<OrderDto> delete(long id) throws RepositoryException {
        service.delete(id);
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.okResponse(ORDER + DELETED))
                .build();
    }

    @Override
    public DtoPage<OrderDto> findByPage(int page, int size, String sort) {
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.okResponse(
                        ORDER + PAGE + "page " + page + ", size" + size + ", sort" + sort
                ))
                .setContent(service.findAll(page,size,sort).stream().map(this::mapper).toList())
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    @Override
    public DtoPage<OrderDto> findById(long id) throws RepositoryException {
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.okResponse(ORDER + FOUND_BY_ID))
                .setContent(List.of(mapper((service.findById(id)))))
                .build();
    }

    @Override
    public DtoPage<OrderDto> findByParam(OrderDto dto) {
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.okResponse(ORDER + FOUND_BY_PARAM))
                .setContent(service.findByParam(dto).stream().map(this::mapper).toList())
                .build();
    }

    @Override
    public DtoPage<OrderDto> findByActiveStatus(int page, int size, String sort) {
        return findByStatus(page,size,sort,ACTIVE.name());
    }

    @Override
    public DtoPage<OrderDto> findByStatus(int page, int size, String sort, String statusName) {
        return new DtoPageBuilder<OrderDto>()
                .setResponse(responseService.okResponse(
                        ORDER + PAGE + "page " + page + ", size" + size + ", sort" + sort
                ))
                .setContent(service.findByStatus(page,size,sort,statusName).stream().map(this::mapper).toList())
                .setSize(size)
                .setNumberOfPage(page)
                .setSortBy(sort)
                .build();
    }

    private OrderDto mapper(Order order) {
        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getPurchaseTime(),
                giftMapper.toDto(order.getGift()),
                order.getGift().getId(),
                order.getUser().getId(),
                order.getStatus()
        );
    }


}
