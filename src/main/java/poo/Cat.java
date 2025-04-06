package poo;

public class Cat extends Animal{

    public Cat(String name, int age){
        super(name, age);
    }
    
    @Override
    public String getName(){
        return super.getName() + "Este es mi nommbre!";
    }

    @Override
    public void eat(){
        System.out.println(getName() + ": ñam ñam ñam");
    }

    @Override
    public void move(){
        System.out.println(getName() + ": running...");
    }


    @Override
    public void makeSound(){
        System.out.println(getName() + ": Miaw! Miaw!");
    }

    public void makeMess(){
        System.out.println(getName() + "Is making a mess...");
    }
}
