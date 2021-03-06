/**
 * 
 */
package datascript.test;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;

import junit.framework.TestCase;
import bits.ItemA;
import bits.ItemB;
import bits.arrays.VarArray;
import bits.VarArrayWithSize;
import datascript.runtime.array.ObjectArray;

/**
 * @author HWellmann
 *
 */
public class SizeOfTest extends TestCase
{
    private FileImageOutputStream os;
    private String wFileName = "SizeOfTest.data";
    private String fileName = "sizeoftest.bin";
    private File file = new File(fileName);

    /**
     * Constructor for BitStreamReaderTest.
     * @param name
     */
    public SizeOfTest(String name)
    {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception
    {
        file.delete();
    }

    /*
     * Test method for 'datascript.library.BitStreamReader.readByte()'
     */
    private int writeArray(int numElems, int startValue) throws IOException
    {
        os = new FileImageOutputStream(file);

        for (int i = 0; i < numElems; i++)
        {
            os.writeByte(1);
            os.writeShort(startValue+i);
        }
        os.writeByte(2);
        os.writeInt(startValue);
        os.writeShort(numElems);
        int size = (int)os.getStreamPosition();
        os.writeShort(size);
        os.writeShort(size*8);

        size = (int)os.getStreamPosition();
        os.close();
        
        return size;
    }
    
    private void checkArray(VarArrayWithSize as, int size, int numElems, int startValue)
    {
        VarArray array = as.getVar();
        ObjectArray<ItemA> aa = array.getA();
        for (int i = 0; i < aa.length(); i++)
        {
            ItemA a = aa.elementAt(i);
            assertEquals(startValue+i, a.getValue());
        }
        ItemB b = array.getB();
        assertEquals(startValue, b.getValue());
        assertEquals(numElems, array.getLen());
        assertEquals(as.getSize(), array.sizeof());
        
        assertEquals(size, as.sizeof());
    }

    public void testArray1() throws Exception
    {
        int size = writeArray(2, 20);
        VarArrayWithSize as = new VarArrayWithSize(fileName);
        checkArray(as, size, 2, 20);

        as.write(wFileName);

        VarArrayWithSize as2 = new VarArrayWithSize(wFileName);
        checkArray(as2, size, 2, 20);
    }

    public void testArray2() throws Exception
    {
        int size = writeArray(10, 2000);
        VarArrayWithSize as = new VarArrayWithSize(fileName);
        checkArray(as, size, 10, 2000);

        as.write(wFileName);

        VarArrayWithSize as2 = new VarArrayWithSize(wFileName);
        checkArray(as2, size, 10, 2000);
    }

    public void testArray3() throws Exception
    {
        int size = writeArray(100, 20000);
        VarArrayWithSize as = new VarArrayWithSize(fileName);
        checkArray(as, size, 100, 20000);

        as.write(wFileName);

        VarArrayWithSize as2 = new VarArrayWithSize(wFileName);
        checkArray(as2, size, 100, 20000);
    }
}
