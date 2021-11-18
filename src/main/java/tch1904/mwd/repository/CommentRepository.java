package tch1904.mwd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import tch1904.mwd.entity.Comment;


public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {

    Page<Comment> findAllByProductIdAndParentIdIsNull(Integer productId, Pageable pageable);
}
