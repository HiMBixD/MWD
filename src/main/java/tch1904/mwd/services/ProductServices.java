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
import tch1904.mwd.controllers.request.*;
import tch1904.mwd.entity.PlayList;
import tch1904.mwd.entity.PlayListProduct;
import tch1904.mwd.entity.Product;
import tch1904.mwd.entity.UserProduct;
import tch1904.mwd.entity.dto.UserDTO;
import tch1904.mwd.entity.dto.UserProductDTO;
import tch1904.mwd.repository.PlayListProductRepository;
import tch1904.mwd.repository.PlayListRepository;
import tch1904.mwd.repository.ProductRepository;
import tch1904.mwd.repository.UserProductRepository;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServices {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonServices commonServices;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PlayListRepository playListRepository;

    @Autowired
    private PlayListProductRepository playListProductRepository;

    @Autowired
    private UserProductRepository userProductRepository;


    public void addProduct(AddProductRequest request) {
        try {
            if (StringUtils.isEmpty(request.getProductAvatar())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "avatar"));
            }
            if (StringUtils.isEmpty(request.getFileId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "fileId"));
            }

            Optional<Product> optional = productRepository.findByFileId(request.getFileId());
            if (optional.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.DUPLICATE, "fileId"));
            }
            Product product = new Product();
            product.setUsername(commonServices.getCurrentUser().getUsername());
            product.setProductType("");
            product.setIsPublished(false);
            product.setTotalBuy(0);
            product.setTotalComment(0);
            product.setTotalView(0);
            product.setTotalMark(0);
            product.setProductAvatar(request.getProductAvatar());
            product.setFileId(request.getFileId());
            product.setProductName(request.getProductName());
            productRepository.save(product);
        }catch (Exception e){
            throw e;
        }
    }

    public void addView(Integer productId) {
        try {
            if (StringUtils.isEmpty(productId)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "productId"));
            }
            Optional<Product> optional = productRepository.findById(productId);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product"));
            }
            Product product = optional.get();
            product.setTotalView(product.getTotalView() + 1);
            productRepository.save(product);
        }catch (Exception e){
            throw e;
        }
    }

    public void markProduct(MarkProductRequest request) {
        try {
            if (StringUtils.isEmpty(request.getMark())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "mark"));
            }
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "productId"));
            }

            Optional<Product> optional = productRepository.findById(request.getProductId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "productId"));
            }
            Optional<UserProduct> userProductOptional = userProductRepository
                            .findByUsernameAndProductId(commonServices.getCurrentUser().getUsername(), request.getProductId());
            UserProduct userProduct = new UserProduct();
            if (userProductOptional.isPresent()) {
                userProduct = userProductOptional.get();
                userProduct.setMark(request.getMark());
            } else {
                userProduct.setProductId(request.getProductId());
                userProduct.setMark(request.getMark());
                userProduct.setUsername(commonServices.getCurrentUser().getUsername());
            }
            userProductRepository.save(userProduct);

            List<UserProduct> markTable = userProductRepository.findAllByProductId(request.getProductId());
            Product product = optional.get();
            product.setMark(markTable.stream().mapToDouble(UserProduct::getMark).average().orElse(0.0));
            product.setTotalMark((markTable.size()));
            productRepository.save(product);
        }catch (Exception e){
            throw e;
        }
    }

    public List<UserProductDTO> getListOwnProduct() {
        try {
            return userProductRepository
                    .findAllByUsername(commonServices.getCurrentUser().getUsername());
        }catch (Exception e){
            throw e;
        }
    }

    public Boolean isOwnProduct(Integer productId) {
        try {
            return userProductRepository
                    .findByUsernameAndProductId(commonServices.getCurrentUser().getUsername(), productId).isPresent();
        }catch (Exception e){
            throw e;
        }
    }

    public void buyProduct(Integer productId) {
        try {
            if (StringUtils.isEmpty(productId)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "productId"));
            }
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "productId"));
            }
            Optional<UserProduct> userProductOptional = userProductRepository
                    .findByUsernameAndProductId(commonServices.getCurrentUser().getUsername(), productId);
            UserDTO userDTO;
            Product product = optionalProduct.get();
            UserProduct userProduct = new UserProduct();
            if (userProductOptional.isEmpty() || !userProductOptional.get().getIsOwn()) {
                userDTO = userService.getUserInfo(commonServices.getCurrentUser().getUsername());
                if (userDTO.getMoney() < product.getPrice()) {
                    throw new AppResponseException(new Message(AppConstants.INVALID, "Money not enough"));
                }
                if (userProductOptional.isPresent()) {
                    userProduct = userProductOptional.get();
                } else {
                    userProduct.setProductId(productId);
                    userProduct.setUsername(commonServices.getCurrentUser().getUsername());
                }
                userProduct.setIsOwn(true);
                product.setTotalBuy(product.getTotalBuy() + 1);
                userProductRepository.save(userProduct);
                // - money from account by
                userService.saveMoney(false, product.getPrice(), commonServices.getCurrentUser().getUsername());
                // add money to account by
                userService.saveMoney(true, product.getPrice() * (1 - AppConstants.BUY_FEE), product.getUsername());
                productRepository.save(product);
            } else if (userProductOptional.get().getIsOwn()) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "Product already own"));
            }

        }catch (Exception e){
            throw e;
        }
    }

    public Page<Product> searchProduct(SearchProductRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPagination().getPageNumber(), request.getPagination().getPageSize());
            return productRepository.searchProduct(
                    request.getUsername(), request.getProductName(), request.getProductType(), request.getIsPublish(), pageable);
        }catch (Exception e){
            throw e;
        }
    }

    public Product getProductInfo(String fileId) {
        try {
            return productRepository
                    .findByFileId(fileId).get();
        }catch (Exception e){
            throw e;
        }
    }

    public void addViewed(Integer productId) {
        try {
            Optional<Product> optional = productRepository.findById(productId);
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product To Add View"));
            }
            Product product = optional.get();
            product.setTotalView(optional.get().getTotalView() + 1);
            productRepository.save(product);
        }catch (Exception e){
            throw e;
        }
    }

    public void createPlayList(CreatePlayListRequest request) {
        try {
            if (StringUtils.isEmpty(request.getTitle())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Title"));
            }
            if (StringUtils.isEmpty(request.getDescription())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Description"));
            }
            Optional<PlayList> optionalPlayList = playListRepository.findByTitle(request.getTitle());
            if (optionalPlayList.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Title"));
            }
            PlayList playList = new PlayList();
            playList.setUsername(commonServices.getCurrentUser().getUsername());
            playList.setDescription(request.getDescription());
            playList.setTitle(request.getTitle());
            playListRepository.save(playList);
        }catch (Exception e){
            throw e;
        }
    }

    public List<PlayList> getAllPlayListsByUsername(SimpleStringRequest request) {
        try {
            if (StringUtils.isEmpty(request.getString())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Username"));
            }
            return playListRepository.findAllByUsername(request.getString());
        }catch (Exception e){
            throw e;
        }
    }

    public List<PlayListProduct> getPlayListItem(Integer listId) {
        try {
            return playListProductRepository.findAllByListId(listId);
        }catch (Exception e){
            throw e;
        }
    }

    public List<PlayListProduct> getRecommendSongs() {
        try {
            return playListProductRepository.getRecommendSongs();
        }catch (Exception e){
            throw e;
        }
    }

    public void addToPlayList(AddOrDeleteItemOfPlayListRequest request) {
        try {
            if (StringUtils.isEmpty(request.getListId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ListId"));
            }
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ProductId"));
            }
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product"));
            }
            Optional<PlayList> optionalPlayList = playListRepository.findById(request.getListId());
            if (optionalPlayList.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "PlayList"));
            }
            Optional<PlayListProduct> optionalPlayListProduct =
                    playListProductRepository
                    .findByProductAndAndListId(optionalProduct.get(), request.getListId());
            if (optionalPlayListProduct.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Song already in this PlayList"));
            }
            PlayListProduct playListProduct = new PlayListProduct();
            playListProduct.setProduct(optionalProduct.get());
            playListProduct.setListId(request.getListId());
            playListProductRepository.save(playListProduct);
        }catch (Exception e){
            throw e;
        }
    }

    public void removeFromPlayList(AddOrDeleteItemOfPlayListRequest request) {
        try {
            if (StringUtils.isEmpty(request.getListId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ListId"));
            }
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "ProductId"));
            }
            Optional<Product> optionalProduct = productRepository.findById(request.getListId());
            if (optionalProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Product"));
            }
            Optional<PlayList> optionalPlayList = playListRepository.findById(request.getListId());
            if (optionalPlayList.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "PlayList"));
            }
            Optional<PlayListProduct> optionalPlayListProduct =
                    playListProductRepository
                            .findByProductAndAndListId(optionalProduct.get(), request.getListId());
            if (optionalPlayListProduct.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Song is not in this PlayList"));
            }
            playListProductRepository.deleteById(optionalPlayListProduct.get().getId());
        }catch (Exception e){
            throw e;
        }
    }
}
