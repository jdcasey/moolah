package org.commonjava.moolah.model;

import java.util.IllegalFormatException;

public class MoolahDataException
    extends Exception
{

    private static final long serialVersionUID = 1L;

    private final Object[] params;

    public MoolahDataException( final String format, final Throwable error, final Object... params )
    {
        super( format, error );
        this.params = params;
    }

    public MoolahDataException( final String format, final Object... params )
    {
        super( format );
        this.params = params;
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessage();
    }

    @Override
    public String getMessage()
    {
        String format = super.getMessage();
        if ( params != null && params.length > 0 )
        {
            try
            {
                format = String.format( format, params );
            }
            catch ( final IllegalFormatException e )
            {
            }
        }

        return format;
    }

}
