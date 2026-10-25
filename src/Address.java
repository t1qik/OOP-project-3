import java.util.Objects;

public class Address {
    private String city;
    private String street;
    private int houseNumb;
    private int height;

    public Address(String city, String street, int houseNumb, int height) {
        this.city = city;
        this.street = street;
        this.houseNumb = houseNumb;
        this.height = height;
    }

    void SetCity(String c){this.city = c;}
    void SetStreet(String c){this.street = c;}
    void SetHouse(int c){this.houseNumb = c;}
    void SetHeight(int c){this.height = c;}

    String getCity(){return city;}
    String getStreet(){return street;}
    int getHouseNumb(){return  houseNumb;}
    int getHeight(){return height;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return houseNumb == address.houseNumb &&
                height == address.height &&
                Objects.equals(city, address.city) &&
                Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, houseNumb, height);
    }

    // Для красивого вывода
    @Override
    public String toString() {
        return city + ", " + street + ", д. " + houseNumb + " (эт. " + height + ")";
    }
}
