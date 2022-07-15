package MyProjectGradle.models.entities;

import MyProjectGradle.models.enums.TypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "type")
public class Type extends BaseEntity{
    @Enumerated(value = EnumType.STRING)
    private TypeEnum type;
    @Lob
    private String description;
    @Column(nullable = false)
    private int capacity;

    public Type() {
    }

    public Type(TypeEnum type, String description, int capacity) {
        this.type = type;
        this.description = description;
        this.capacity = capacity;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
