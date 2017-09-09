import java.util.* ;
import java.awt.image.* ;
import javax.imageio.ImageIO;
import java.io.*;

class feedforward
{
    public static void main(String[] args) throws IOException
    {
    
    double[] ayy = feedForward(readImageFile(args[0]),300,"weights/hidden-weights.txt");
    System.out.println(indexHighest(feedForward(ayy,10,"weights/output-weights.txt")));
    System.out.println("Bonus:");
    System.out.println("Classification Rate: " + bonus());
    
    }
    
    public static double sigmoidFunction(double x)
    {
        return (1/(1+Math.pow(Math.E,-x)));
    }
    
    public static double[] readImageFile(String name) throws IOException
    {
    
    BufferedImage img = ImageIO.read(new File("numbers/" + name));
     // get pixel data
     double[] dummy = null;
     double[] X = img.getData().getPixels(0, 0, img.getWidth(),img.getHeight(), dummy);
     for (int i = 0 ; i < X.length;i++)
     {
         X[i] = X[i]/255 ;
     }
     return X ;
    }
    
    public static double[] feedForward(double [] input,int x,String file) throws FileNotFoundException
    {
        ArrayList<String[]> weights = fileReader(file);
        double[] hidden = new double[x];
        ArrayList<Double> out = new ArrayList<Double>();
        
        for (int i = 0 ; i < hidden.length; i++)
        {
            double perc = 0;
            for (int j = 0 ; j < input.length-1;j++)
            {
               perc += Double.parseDouble(weights.get(i)[j]) * input[j];
            }
            perc += (double) input[input.length-1];
            
            out.add(sigmoidFunction(perc));
        }
       double[] outArray = new double[out.size()];
       
       for (int k = 0 ; k < out.size();k++)
       {
           outArray[k] = out.get(k);
       }
       //System.out.println(Arrays.toString(outArray));
       return outArray;

    }
    
    public static ArrayList<String[]> fileReader(String file) throws FileNotFoundException
    {
        File txt = new File(file);
        Scanner in = new Scanner(txt);
        ArrayList<String[]> out = new ArrayList<String[]>();
        while (in.hasNextLine())
        {
            out.add(in.nextLine().split(" "));
        }
        return out;
    }
    public static int indexHighest(double[] input)
    {
        int index = 0;
        double largest = 0;
        for (int i = 0; i < input.length;i++)
        {
            if (input[i] > largest || i == 0)
            {
                largest = input[i];
                index = i;
            }
        }
        return index;
    }
    public static double bonus() throws IOException
    {
        ArrayList<String[]> arr = fileReader("labels.txt");
        ArrayList<Integer> count = new ArrayList<Integer>();

        for (String[] i : arr)
        {
        double[] inpLayer = feedForward(readImageFile(i[0]),300,"weights/hidden-weights.txt");
        int index = indexHighest(feedForward(inpLayer,10,"weights/output-weights.txt"));
        if (Integer.parseInt(i[1]) == index)
            {
            count.add(1);
            }
        }
        return (double) count.size()/arr.size();
       
    }
}