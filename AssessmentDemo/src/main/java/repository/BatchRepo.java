package repository;

import org.springframework.data.repository.CrudRepository;

import com.epam.model.Batch;

public interface BatchRepo extends CrudRepository<Batch,Integer>{

}
