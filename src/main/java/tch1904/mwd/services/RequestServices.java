package tch1904.mwd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.controllers.request.SearchRequestListRequest;
import tch1904.mwd.entity.RequestAddMoney;
import tch1904.mwd.entity.RequestBeSinger;
import tch1904.mwd.entity.RequestPublishProduct;
import tch1904.mwd.repository.RequestAddMoneyRepository;
import tch1904.mwd.repository.RequestBeSingerRepository;
import tch1904.mwd.repository.RequestPublishProductRepository;

@Service
public class RequestServices {
    @Autowired
    private RequestAddMoneyRepository requestAddMoneyRepository;

    @Autowired
    private RequestBeSingerRepository requestBeSingerRepository;

    @Autowired
    private RequestPublishProductRepository requestPublishProductRepository;

    public Page<RequestAddMoney> findRequestAddMoney(SearchRequestListRequest request) {
        try {
            if (StringUtils.isEmpty(request)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "request"));
            }
            Pageable pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getPageSize());
            return requestAddMoneyRepository.searchListRequestAddMoney(request.getUsername(), request.getStatus(), pageable);
        }catch (Exception e){
            throw e;
        }
    }

    public Page<RequestBeSinger> findRequestBeSinger(SearchRequestListRequest request) {
        try {
            if (StringUtils.isEmpty(request)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "request"));
            }
            Pageable pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getPageSize());
            return requestBeSingerRepository.searchListRequestBeSinger(request.getUsername(), request.getStatus(), pageable);
        }catch (Exception e){
            throw e;
        }
    }

    public Page<RequestPublishProduct> findRequestPublishProduct(SearchRequestListRequest request) {
        try {
            if (StringUtils.isEmpty(request)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "request"));
            }
            Pageable pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getPageSize());
            return requestPublishProductRepository.searchListRequestPublishProduct(request.getUsername(), request.getStatus(), pageable);
        }catch (Exception e){
            throw e;
        }
    }
}
