import java.lang.*;

public class CMain extends java.lang.Object
    
{
    static frMain fr;
    
    public static void main ( String args [] ) {

       fr = new frMain( );
       fr.show();
       

    }
    
    public static void DestroyFrame ()
    {
        fr = null;
        System.exit ( 0 );
    }
}