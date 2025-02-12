package com.example.dev_dojo.producer;

import com.example.dev_dojo.domain.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

// Tenta fazer o mapeamento de um objeto (source) para outro (target).

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    // Source é o que está dentro dos parâmetros, e o target é o retorno
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    // @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    Producer toProducer(ProducerPostRequest postRequest);
    Producer toProducer(ProducerPutRequest putRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producerList);
}
