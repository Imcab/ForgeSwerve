package lib.Component;

import java.util.Map;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public interface Component2d{
    Map<String, MechanismLigament2d> getLigaments();
    Mechanism2d getMechanism2d();
    MechanismRoot2d getMechanismRoot();
   
    default void createComponent2d(double width, double height, String rootName, double rootX, double rootY) {
        Mechanism2d mechanism = new Mechanism2d(width, height);
        MechanismRoot2d root = mechanism.getRoot(rootName, rootX, rootY);
        getLigaments().clear(); 
        storeComponent(mechanism, root);
    }

    void storeComponent(Mechanism2d mechanism, MechanismRoot2d root);

    default void ligamentAdd(String name, double length, double angle) {
        MechanismLigament2d ligament = getMechanismRoot().append(new MechanismLigament2d(name, length, angle));
        getLigaments().put(name, ligament);
    }

    default boolean ligamentExists(String name) {
        return getLigaments().containsKey(name);
    }

    default void ligamentSetColorRGB(String name, int r, int g, int b) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setColor(new Color8Bit(r, g, b));
        }
    }

    default void ligamentColor(String name, Color color) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setColor(new Color8Bit(color));
        }
    }

    default void ligamentUpdateAngle(String name, double angle) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setAngle(angle);
        }
    }

    default void ligamentUpdateAngle(String name, Rotation2d angle) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setAngle(angle);
        }
    }

    default void ligamentLUpdateLenght(String name, double length) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setLength(length);
        }
    }

    default void ligamentUpdateBoth(String name, double angle, double length) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setAngle(angle);
            getLigaments().get(name).setLength(length);
        }
    }

    default void ligamentUpdateBoth(String name, Rotation2d angle, double length) {
        if (ligamentExists(name)) {
            getLigaments().get(name).setAngle(angle);
            getLigaments().get(name).setLength(length);
        }
    }

    default void ligamentReset() {
        getLigaments().values().forEach(l -> {
            l.setLength(0);
            l.setAngle(0);
        });
    }

    default MechanismLigament2d getLigament(String name) {
        return getLigaments().get(name);
    }

    default void ligamentBlink(String name, Color colorA, Color colorB, double speedHz, double time) {
        if (ligamentExists(name)) {
            boolean toggle = ((int)(time * speedHz) % 2 == 0);
            Color color = toggle ? colorA : colorB;
            getLigaments().get(name).setColor(new Color8Bit(color));
        }
    }

    void updateComponent();

}
