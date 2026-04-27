//package com.app.penjahit.util;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FireStoreUtil {
//    private BaseView parent;
//
//    public FireStoreUtil(BaseView activity) {
//        parent = activity;
//    }
//
//    public void insertDataWithoutId(String loadingDesc, String tableName, HashMap<String,
//            String> data, CallbackFireStore.TransactionData callback) {
//        parent.showLoading(loadingDesc);
//        FirebaseFirestore.getInstance().collection(tableName).document().set(data)
//                .addOnSuccessListener(aVoid -> {
//                    parent.hideLoading();
//                    callback.onSuccessTransactionData();
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                    parent.hideLoading();
//                    callback.onFailedTransactionData(e.getMessage());
//                });
//    }
//
//    public void insertDataObject(String loadingDesc, String tableName, String id, HashMap<String,
//            Object> data, CallbackFireStore.TransactionData callback) {
//        parent.showLoading(loadingDesc);
//        FirebaseFirestore.getInstance().collection(tableName).document(id).set(data)
//                .addOnSuccessListener(aVoid -> {
//                    parent.hideLoading();
//                    callback.onSuccessTransactionData();
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                    parent.hideLoading();
//                    callback.onFailedTransactionData(e.getMessage());
//                });
//    }
//
//    public void insertData(String tableName, String id, HashMap<String,
//            String> data, CallbackFireStore.TransactionData callback) {
//        FirebaseFirestore.getInstance().collection(tableName).document(id).set(data)
//                .addOnSuccessListener(aVoid -> {
//                    callback.onSuccessTransactionData();
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                    callback.onFailedTransactionData(e.getMessage());
//                });
//    }
//
//    public void deleteDataById(String loadingDesc, String tableName, String id
//            , CallbackFireStore.TransactionData callback) {
//        parent.showLoading(loadingDesc);
//        FirebaseFirestore.getInstance().collection(tableName).document(id).delete()
//                .addOnSuccessListener(aVoid -> {
//                    parent.hideLoading();
//                    callback.onSuccessTransactionData();
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                    parent.hideLoading();
//                    callback.onFailedTransactionData(e.getMessage());
//                });
//    }
//    public void deleteDataById( String tableName, String id) {
//        FirebaseFirestore.getInstance().collection(tableName).document(id).delete()
//                .addOnSuccessListener(aVoid -> {
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                });
//    }
//
//
//    public void readDataSpecificCondition(
//            String loadingDesc,
//            String tableName,
//            String queryKey,
//            String queryValue,
//            CallbackFireStore.ReadData callback) {
//        if(!loadingDesc.isEmpty()){
//            parent.showLoading(loadingDesc);
//        }
//        FirebaseFirestore.getInstance().collection(tableName).
//                whereEqualTo(queryKey, queryValue)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if(!loadingDesc.isEmpty()) {
//                            parent.hideLoading();
//                        }
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        if(!loadingDesc.isEmpty()) {
//                            parent.hideLoading();
//                        }
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//
//    public void readAllData(
//            String loadingDesc,
//            String tableName,
//            CallbackFireStore.ReadData callback) {
//        if(!loadingDesc.isEmpty()) {
//            parent.showLoading(loadingDesc);
//        }
//        FirebaseFirestore.getInstance().collection(tableName)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        parent.hideLoading();
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        parent.hideLoading();
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//    public void readAllDataOrderBy(
//            String loadingDesc,
//            String tableName,
//            String orderBy,
//            CallbackFireStore.ReadData callback) {
//        if(!loadingDesc.isEmpty()) {
//            parent.showLoading(loadingDesc);
//        }
//        FirebaseFirestore.getInstance().collection(tableName).orderBy(orderBy, Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        parent.hideLoading();
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        parent.hideLoading();
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//
//    public void readAllData(
//            String tableName,
//            CallbackFireStore.ReadData callback) {
//        FirebaseFirestore.getInstance().collection(tableName)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//
//    public void readDataByDocumentId(
//            String loadingDesc,
//            String tableName,
//            String documentId,
//            boolean isShowLoading,
//            CallbackFireStore.ReadDocumentData callback) {
//        if (isShowLoading) {
//            parent.showLoading(loadingDesc);
//        }
//        FirebaseFirestore.getInstance().collection(tableName).document(documentId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if (isShowLoading) {
//                            parent.hideLoading();
//                        }
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        if (isShowLoading) {
//                            parent.hideLoading();
//                        }
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//
//    public void updateData(String loadingDesc, String tableName, String id, Map<String, Object> data, CallbackFireStore.UpdateDocumentData callback) {
//        if(!loadingDesc.isEmpty()){
//            parent.showLoading(loadingDesc);
//        }
//        FirebaseFirestore.getInstance().collection(tableName).document(id)
//                .update(data)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful())
//                    {
//                        callback.onSuccessUpdate();
//                    }
//                    else {
//                        callback.onFailedUpdate(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                    if(!loadingDesc.isEmpty()) {
//                        parent.hideLoading();
//                    }
//                });
//    }
//
//    public void readDataLogin(
//            String loadingDesc,
//            String tableName,
//            String username,
//            String password,
//            CallbackFireStore.ReadData callback) {
//        parent.showLoading(loadingDesc);
//        FirebaseFirestore.getInstance().collection(tableName).
//                whereEqualTo("username", username).
//                whereEqualTo("password", password)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        parent.hideLoading();
//                        callback.onSuccessReadData(task.getResult());
//                    } else {
//                        parent.hideLoading();
//                        callback.onFailedReadData(task.getException().getMessage());
//                        task.getException().printStackTrace();
//                    }
//                });
//    }
//
//}
