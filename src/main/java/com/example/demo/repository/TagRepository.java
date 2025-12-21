package com.example.demo.repository;

import com.example.demo.entity.Tag;
import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
