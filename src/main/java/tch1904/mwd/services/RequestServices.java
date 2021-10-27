package tch1904.mwd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.controllers.request.SearchRequestAddMoneyRequest;
import tch1904.mwd.entity.RequestAddMoney;
import tch1904.mwd.repository.RequestAddMoneyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServices {
    @Autowired
    private RequestAddMoneyRepository requestAddMoneyRepository;

    public List<RequestAddMoney> findRequestAddMoney(SearchRequestAddMoneyRequest request) {
        try {
            if (StringUtils.isEmpty(request)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "request"));
            }
            return requestAddMoneyRepository.searchListRequestAddMoney(request.getUsername(), request.getStatus());
        }catch (Exception e){
            throw e;
        }
    }
}
