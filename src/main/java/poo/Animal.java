package poo;

public abstract class Animal {
    
    String name;
    int age;

    public Animal(String AnimalName, int AnimalAge){
        this.name = AnimalName;
        this.age = AnimalAge;
    }

    public abstract void makeSound();
    public abstract void move();
    public abstract void eat();

    public int getAge(){
        return age;
    }

    public String getName(){
        return name;
    }

    public void displayInfo() {
        System.out.println("Nombre: " + name + ", Edad: " + age);
    }

    

}
