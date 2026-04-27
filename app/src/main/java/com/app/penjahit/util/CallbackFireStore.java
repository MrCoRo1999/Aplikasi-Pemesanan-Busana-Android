package com.app.penjahit.util;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CallbackFireStore {
     public interface TransactionData {
        void onSuccessTransactionData();
        void onFailedTransactionData(String message);
    }
    public interface ReadData {
        void onSuccessReadData(QuerySnapshot document );
        void onFailedReadData(String message);
    }
    public interface ReadDocumentData {
        void onSuccessReadData(DocumentSnapshot document );
        void onFailedReadData(String message);
    }
    public interface UpdateDocumentData{
         void onSuccessUpdate();
         void onFailedUpdate(String message);
    }
}
