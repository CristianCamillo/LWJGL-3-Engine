package engine;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils
{
    public static String loadResource(String fileName) throws Exception
    {      
        Scanner scanner = new Scanner(new File(fileName), "UTF-8");        
        String result = scanner.useDelimiter("\\A").next();        
        scanner.close();        
        return result;
    }
    
    public static List<String> readAllLines(String fileName) throws Exception
    {
        List<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
        
        while(scanner.hasNextLine())
            list.add(scanner.nextLine());
        
        scanner.close();

        return list;
    }
    
    public static float[] listToArray(List<Float> list)
    {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for(int i = 0; i < size; i++)
            floatArr[i] = list.get(i);
        
        return floatArr;
    }
    
    public static int[] listIntToArray(List<Integer> list)
    {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }
    
    public static boolean existsResourceFile(String fileName)
    {
        boolean result;
        try(InputStream is = Utils.class.getResourceAsStream(fileName ))
        {
            result = is != null;
        }
        catch (Exception e)
        {
            result = false;
        }
        return result;
    }
}