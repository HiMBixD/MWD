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
import tch1904.mwd.controllers.request.SearchRequestAddMoneyRequest;
import tch1904.mwd.controllers.request.SimpleStringRequest;
import tch1904.mwd.entity.RequestAddMoney;
import tch1904.mwd.repository.RequestAddMoneyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServices {
    @Autowired
    private RequestAddMoneyRepository requestAddMoneyRepository;

    public Page<RequestAddMoney> findRequestAddMoney(SearchRequestAddMoneyRequest request) {
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
}
