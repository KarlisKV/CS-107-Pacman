/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        14:39
 */


package ch.epfl.cs107.play.io;

import java.io.*;

public class Serialization implements Serializable {

    /**
     * Method to serialize Object into /tmp
     * @param serializableObject the Object to serialize
     * @param fileOutName        the new .ser filename
     */
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

    /**
     * Method to read serialized file into an Object
     * @param fileName the .ser filename that should be read
     * @return an Object from the .ser file or (null) if any exception happened
     */
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

    /**
     * Method to delete a file in /tmp
     * @param fileName the name of the file to be deleted
     */
    public static void delete(String fileName) {
        File directory = new File("tmp");
        File file = new File(directory, fileName);
        file.delete();
    }
}
