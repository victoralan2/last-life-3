package net.olimpium.last_life_iii.utils;

import org.bukkit.*;

public class ParticleSphere{

    public double definition = 20;
    public double SphereRadius = 10;
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public Location location;

    public ParticleSphere(Location location, double shpereRadius, double definition, int red, int green, int blue){
        generateSphere();
    }

    public void generateSphere() {
        for (double i = 0; i <= Math.PI; i += Math.PI / this.definition) {
            double radius = Math.sin(i)*this.SphereRadius;
            double y = Math.cos(i)*this.SphereRadius;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / this.definition) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                this.location.add(x, y, z);
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(this.red, this.green, this.blue), 3f);
                int random = 1;
                this.location.getWorld().spawnParticle(Particle.REDSTONE, this.location, random, 0, 0, 0, 2, dustOptions, false);
                this.location.subtract(x, y, z);
            }
        }
    }


}
