package org.commonjava.moolah.data;

import org.commonjava.couch.db.model.ViewRequest;

public class MoolahViewRequest
    extends ViewRequest
{

    public MoolahViewRequest( final MoolahView view )
    {
        super( MoolahAppDescription.APP_NAME, view.viewName() );
    }

}
