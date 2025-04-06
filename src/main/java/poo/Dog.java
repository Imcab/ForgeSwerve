package poo;

public class Dog extends Animal{

    public Dog(String name, int age){
        super(name, age);
    }

    @Override
    public void eat(){
        System.out.println(getName() + ": comer... comer... comer...");
    }

    @Override
    public void move(){
        System.out.println(getName() + ": walking...");
    }

    @Override
    public void makeSound(){
        System.out.println(getName() + ": Woof! Woof!");
    }

    public void greet(){
        System.out.println(getName() + ": Is saying hello!");
    }
}
