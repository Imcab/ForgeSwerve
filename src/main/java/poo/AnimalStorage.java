package poo;

import java.util.ArrayList;
import java.util.List;

public class AnimalStorage {

    private List<Animal> animalRegister;

    private StringBuilder builder = new StringBuilder("Animal Names: ");
 
    public AnimalStorage(Animal... animals){
        this.animalRegister = new ArrayList<>(List.of(animals));
    }

    public int getStoredAnimals(){
        return animalRegister.size();
    }

    public String getNames(){
        
        for(Animal animals : animalRegister){
            builder.append(animals.getName() + ", ");
        }

        return builder.toString().replaceAll(", $", ", ");
    }

    public String getType(){

        //0 for cats, 1 for dogs, etc
        int animalType = -1;
        String type = "";

        for(Animal animal : animalRegister){
            if (animal instanceof Cat) {
                type = "Cats";
                animalType = 0;
            }else if (animal instanceof Dog) {
                type = "Dog";
                animalType = 1;
            }else{
                type = "";
            }
        }

        if (animalType == 0) {
            return type + " in: House";
        }

        if (animalType == 1) {
            return type + " in: DogHouse";
        }

        return "";


    }

    public void feed(int index){
        if (index >= 0 && index < animalRegister.size()) {
            animalRegister.get(index).eat();
        }else{
            System.out.println("Animal in index: " + index + "not existing!");
        }
    }

}
