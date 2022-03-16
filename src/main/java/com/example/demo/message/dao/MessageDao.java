package com.example.demo.message.dao;

import com.example.demo.message.dto.MessageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends CrudRepository<MessageDto, Integer> {

    List<MessageDto> findByUserIdOrderByIdDesc(Integer userId, Pageable pageable);

}
