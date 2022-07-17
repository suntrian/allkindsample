package org.example.compressSample;

import net.sf.sevenzipjbinding.ArchiveFormat;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.util.ByteArrayStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {@link <a href="http://sevenzipjbind.sourceforge.net/first_steps.html">site</a>}
 */
public class DeCompressUtil {

    public static void deCompress(String filePath) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        IInArchive iInArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(file));
        ArchiveFormat archiveFormat = iInArchive.getArchiveFormat();
        int numberOfItems = iInArchive.getNumberOfItems();
        /*
        iInArchive.extract(new int[]{0, 1}, false, new IArchiveExtractCallback() {
            @Override
            public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
                return null;
            }

            @Override
            public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {

            }

            @Override
            public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {

            }

            @Override
            public void setTotal(long total) throws SevenZipException {

            }

            @Override
            public void setCompleted(long complete) throws SevenZipException {

            }
        });
        */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayStream sequentialOutStream = new ByteArrayStream(Integer.MAX_VALUE);
        ExtractOperationResult extractOperationResult = iInArchive.extractSlow(0, sequentialOutStream);
        extractOperationResult.name();
        sequentialOutStream.writeToOutputStream(outputStream, true);
        System.out.println(outputStream);
    }

}
