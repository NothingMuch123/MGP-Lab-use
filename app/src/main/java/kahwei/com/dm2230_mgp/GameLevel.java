package kahwei.com.dm2230_mgp;

/**
 * Created by xecli on 12/5/2015.
 */
public class GameLevel
{
    String m_levelName;
    // List of Enemy Instructions
    int m_levelScore;               // Score acquired on this level. If 0, means level was never completed.

    GameLevel()
    {

    }

    String GetLevelName()
    {
        return m_levelName;
    }

    int GetScore()
    {
        return m_levelScore;
    }

    boolean IsCompleted()
    {
        return m_levelScore > 0;
    }
}
