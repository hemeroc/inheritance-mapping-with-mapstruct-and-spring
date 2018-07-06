package io.github.hemeroc.mapstructsample;

import lombok.Data;
import lombok.ToString;
import org.mapstruct.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.mapstruct.MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG;

@SpringBootApplication
public class MapstructSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapstructSampleApplication.class, args);
    }
}

@Component
class Runner implements CommandLineRunner {

    private final UserMapper entityMapper;

    Runner(UserMapper entityMapper) {
        this.entityMapper = entityMapper;
    }

    @Override
    public void run(String... args) {
        Entity entity = new Entity();
        entity.setId(1L);
        entity.setUuid(UUID.randomUUID());
        EntityDto entityDto = entityMapper.entityToEntityDto(entity);
        System.out.println(entityDto);
        System.out.println(entityMapper.entityDtoToEntity(entityDto));
    }

}

@Data
abstract class BaseEntity {

    protected Long id;
    protected UUID uuid;

}

@Data
@ToString(callSuper = true)
class Entity extends BaseEntity {

    private String someText;

}

@Data
class BaseEntityDto {

    private String uuid;

}

@Data
@ToString(callSuper = true)
class EntityDto extends BaseEntityDto {

    private String someText;

}


@Mapper(config = BaseEntityMapper.class)
interface UserMapper {

    EntityDto entityToEntityDto(Entity entity);

    Entity entityDtoToEntity(EntityDto dto);

}

@MapperConfig(
        uses = BaseEntityMapper.BaseEntityMapperDefaults.class,
        mappingInheritanceStrategy = AUTO_INHERIT_FROM_CONFIG
)
interface BaseEntityMapper {

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
    })
    BaseEntityDto entityToDto(BaseEntity entity);

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "id", ignore = true),
    })
    BaseEntity dtoToEntity(BaseEntityDto dto);

    @Mapper
    interface BaseEntityMapperDefaults {

        default String uuidToString(UUID uuid) {
            return uuid.toString();
        }

        default UUID stringToUuid(String string) {
            return UUID.fromString(string);
        }
    }

}