package engine;

public interface ILogic
{
    void init(Window window) throws Exception;
    
    void input(Window window, KeyboardInput keyboardInput, MouseInput mouseInput);

    void update(float elapsedTime);
    
    void render(Window window, float fps);
    
    void cleanup();
    
    boolean wantToClose();
}