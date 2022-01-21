package me.ayouub.bodypartsapi;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class BodyPartsAPI extends JavaPlugin  {
    /**
     * Rotates a vector around the Y axis.
     * @param vector the vector that is to rotate
     * @param degrees the Angle of rotating
     * @return the rotated vector.
     */
    public static Vector rotateAroundAxisY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);
        double currentX = vector.getX();
        double currentZ = vector.getZ();
        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);
        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }
    /**
     * Gets the vector between a player and a location then
     * rotates it to be as the player's yaw is 0 so it can
     * check the body parts locations
     * @param p the player
     * @param l the location
     * @return the rotated vector.
     */
    public static Vector getBaseVector(Player p, Location l) {
        Location elocation = p.getLocation();
        double yawAngle = elocation.getYaw();
        Vector v = l.toVector().subtract(elocation.toVector());
        return rotateAroundAxisY(v, -yawAngle);
    }
    /**
     * Simply gets the body part of a player from a location using
     * the method getBaseVector and some values that were calculated from
     * the pixels
     * @param p the player
     * @param loc the location
     * @return the body part where the location is, if the location is
     * not in any body part it will simply return null.
     */
    public static BodyPart locationToBodyPart(Player p, Location loc) {
        Vector baseVector = getBaseVector(p, loc);
        double x = baseVector.getX();
        double y = baseVector.getY();
        double z = baseVector.getZ();
        if (b(-0.25, x, 0.25) && b(1.50001, y, 1.92) && b(-0.25, z, 0.25)) return BodyPart.HEAD;
        if (!b(-0.125, z, 0.125)) return null;
        if (b(0, x, 0.25) && b(0, y, 0.75)) return BodyPart.LEFT_LEG;
        if (b(-0.25, x, -0.0001) && b(0, y, 0.75)) return BodyPart.RIGHT_LEG;
        if (b(-0.25, x, 0.25) && b(0.75001, y, 1.5)) return BodyPart.BODY;
        if (b(0.25001, x, 0.4375) && b(0.75001, y, 1.5)) return BodyPart.LEFT_ARM;
        if (b(-0.4375, x, -0.25001) && b(0.75001, y, 1.5)) return BodyPart.RIGHT_ARM;
        return null;
    }
    /**
     * Does the opposite of the method locationToBodyPart so it gets
     * the location of a body part of a said player
     * @param p the player
     * @param part the location
     * @return the body part's location.
     */
    public static Location getBodyPartLocation(Entity p, BodyPart part) {
        Vector v;
        switch (part) {
            case HEAD:
                v = new Vector(0, 1.92, 0);
                break;
            case LEFT_ARM:
                v = new Vector(-0.35, 0.751, 0);
                break;
            case RIGHT_ARM:
                v = new Vector(0.35, 0.751, 0);
                break;
            case BODY:
                v = new Vector(0, 0.8, 0);
                break;
            case LEFT_LEG:
                v = new Vector(-0.125, 0, 0);
                break;
            case RIGHT_LEG:
                v = new Vector(0.125, 0, 0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + part);
        }
        Vector frombase = rotateAroundAxisY(v, p.getLocation().getYaw());
        return p.getLocation().add(frombase);
    }
    /**
     * Checks if the value b is between a and c
     * @param a the player
     * @param b the location
     * @param c the location
     * @return if b is between a and c.
     */
    static boolean b(double a, double b, double c) {return a < b && c > b;}

    public enum BodyPart {
        RIGHT_LEG, LEFT_LEG, BODY, RIGHT_ARM, LEFT_ARM, HEAD
    }
}
