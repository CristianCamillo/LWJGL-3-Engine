package engine;

public class Timer
{
    private double lastLoopTime;
    
    public void init()
    {
        lastLoopTime = getTime();
    }

    public double getTime() 		{ return System.nanoTime() / 1_000_000_000.0; }
    public double getLastLoopTime() { return lastLoopTime; }
    
    public float getElapsedTime()
    {
        double time = getTime();
        double elapsedTime = time - lastLoopTime;
        lastLoopTime = time;
        return (float)elapsedTime;
    }   
}