package net.olimpium.last_life_iii.utils;

public class Loc {


        private double X;
        private double Y;
        private double Z;

        public Loc() {
        }

        public Loc(double X, double Y, double Z) {
            this.X = X;
            this.Y = Y;
            this.Z = Z;
        }
        public double getX(){
            return X;
        }
        public double getY(){
            return Y;
        }
        public double getZ(){
            return Z;
        }
        public Double[] getCoords(){
            Double[] coords = {X, Y, Z};
            return coords;
        }


}
