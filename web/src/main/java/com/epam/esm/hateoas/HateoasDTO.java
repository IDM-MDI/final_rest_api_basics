package com.epam.esm.hateoas;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

public abstract class HateoasDTO<D> {
    private static final String PREVIOUS_PAGE = "prev";
    private static final String NEXT_PAGE = "next";
    public abstract void addLinks(D dto) throws ServiceException, RepositoryException;

    protected abstract void addPageLink(DtoPage<D> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException;

    public void setHateoas(DtoPage<D> page) throws ServiceException, RepositoryException {
        if(page == null || page.getContent() == null) {
            return;
        }

        for (D dto : page.getContent()) {
            addLinks(dto);
        }
        setPageHateoas(page);
    }

    protected void setPageHateoas(DtoPage<D> dtoPage) throws ServiceException, RepositoryException {
        int thisSize = dtoPage.getSize();
        int thisPage = dtoPage.getNumberOfPage();
        int prevPage = thisPage - 1;
        int nextPage = thisPage + 1;
        String thisSort = dtoPage.getSortBy();
        String thisDirection = dtoPage.getDirection();

        if(prevPage >= 0)
            addPageLink(dtoPage,prevPage, thisSize, thisSort, thisDirection, PREVIOUS_PAGE);
        if(dtoPage.isHasNext())
            addPageLink(dtoPage,nextPage, thisSize, thisSort, thisDirection, NEXT_PAGE);
    }
}
