/**
 * @Author: liyu.guan
 * @Date: 2019/5/29 下午4:39
 */
public class Blog {

    private String name;

    private int age;

    private String address;

    private String ID;


    private Blog(Builder builder) {
        this.name = builder.name;

        this.age = builder.age;

        this.address = builder.address;

        this.ID = builder.ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public static class Builder {

        private String name;

        private int age;

        private String address;

        private String ID;


        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setID(String ID) {
            this.ID = ID;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }

        public static void main(String[] args) {
            Blog blog = new Blog.Builder().setAddress("address").setAge(10).build();
        }
    }
}






