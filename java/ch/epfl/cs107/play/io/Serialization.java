/*
 *	Author:      Leonard Cseres
 *	Date:        06.12.20
 *	Time:        14:39
 */


package ch.epfl.cs107.play.io;

import java.io.*;

public final class Serialization {
    private static final String WORKING_DIR = "tmp";

    private Serialization() {
        throw new IllegalStateException("Serialization class");
    }

    /**
     * Method to serialize Object into /tmp
     * @param serializableObject the Object to serialize
     * @param fileOutName        the new .ser filename
     */
    public static void serialize(Object serializableObject, String fileOutName) {
        try {
            File directory = new File(WORKING_DIR);
            File file = new File(directory, fileOutName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(file));
            fileOut.writeObject(serializableObject);
            fileOut.close();
            System.out.println("Successfully saved " + fileOutName + " in /" + WORKING_DIR);
        } catch (IOException i) {
            System.out.println("An ERROR occurred while serializing " + fileOutName + "...");
            i.printStackTrace();
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
            File directory = new File(WORKING_DIR);
            File file = new File(directory, fileName);
            ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(file));
            object = fileIn.readObject();
            fileIn.close();
        } catch (FileNotFoundException f) {
            System.out.println(
                    f.getLocalizedMessage() + " -> file is not yet saved in /" + WORKING_DIR + ", so created new object for \"" +
                            fileName.substring(0, fileName.length() - 4) + "\"");
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("An ERROR occurred while deserializing \"" + fileName + "\"...");
            i.printStackTrace();
        }
        return object;
    }

    /**
     * Method to delete a file in /tmp
     * @param fileName the name of the file to be deleted
     */
    public static void delete(String fileName) {
        File directory = new File(WORKING_DIR);
        File file = new File(directory, fileName);
        file.delete();
    }
}
