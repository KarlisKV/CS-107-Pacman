/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        14:39
 */


package ch.epfl.cs107.play.game.superpacman;

import java.io.*;

public class Serialization implements Serializable {
    public static void serialize(Object serializableObject, String fileOutName) {
        try {
            File directory = new File("tmp");
            File file = new File(directory, fileOutName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(file));
            fileOut.writeObject(serializableObject);
            fileOut.close();
        } catch (IOException i) {
            // Empty on purpose, do noting
        }
    }

    public static Object deserialize(String fileName) {
        Object object = null;
        try {
            File directory = new File("tmp");
            File file = new File(directory, fileName);
            ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(file));
            object = fileIn.readObject();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            // Empty on purpose, do noting
        }
        return object;
    }

    public static void delete(String fileName) {
        File directory = new File("tmp");
        File file = new File(directory, fileName);
        file.delete();
    }
}
