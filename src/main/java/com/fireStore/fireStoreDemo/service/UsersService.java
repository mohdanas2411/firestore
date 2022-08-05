package com.fireStore.fireStoreDemo.service;

import com.fireStore.fireStoreDemo.entity.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Lazy
    @Autowired
    private Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public String saveUser(Users user) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = firestore.collection(COLLECTION_NAME).document(user.getUserId() + "").set(user);

        return apiFuture.get().getUpdateTime().toString();

    }

    public List<Users> getUsers() throws ExecutionException, InterruptedException {
        // Firestore firestore = FirestoreClient.getFirestore();

        //get collection
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection(COLLECTION_NAME).get();


        //document snapshots
        QuerySnapshot queryDocumentSnapshots = querySnapshotApiFuture.get();


        //list of all documents
        List<QueryDocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

        //first filter check if document is not null then it passes to map opeator
        // map documents to Users.class type
        List<Users> users = documents.stream().filter(DocumentSnapshot::exists).map(document -> document.toObject(Users.class)).collect(Collectors.toList());

        // DocumentSnapshot::exists == documents -> documents.exists()

        return users;

        // or we can do this in one line without storing it operation reference
        //first filter check if document is not null then it passes to map opeator
        //return firestore.collection(COLLECTION_NAME).get().get().getDocuments().stream().filter(document -> document.exists()).map(document -> document.toObject(Users.class)).collect(Collectors.toList());
    }


    public Users getUserById(String id) throws ExecutionException, InterruptedException {
        //  Firestore firestore = FirestoreClient.getFirestore();

        String name = "mohd";


        return firestore.collection(COLLECTION_NAME).document(id).get().get().toObject(Users.class);
    }

    public List<Users> findByUserName(String name) throws ExecutionException, InterruptedException {
       return firestore.collection(COLLECTION_NAME).whereEqualTo("userName", name).get().get().getDocuments().stream().filter(doc -> doc.exists()).map(doc -> doc.toObject(Users.class)).collect(Collectors.toList());
    }


    public String getUpdatedTime(String id) throws ExecutionException, InterruptedException {
        //  Firestore firestore = FirestoreClient.getFirestore();

        Timestamp updateTime = firestore.collection(COLLECTION_NAME).document(id).get().get().getUpdateTime();


        assert updateTime != null;
        ZonedDateTime instant = ZonedDateTime.of(updateTime.toSqlTimestamp().toLocalDateTime(), ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        return instant.format(formatter);
    }


    public String deleteUserById(String id) throws ExecutionException, InterruptedException {
        //  Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot documentSnapshot = firestore.collection(COLLECTION_NAME).document(id).get().get();

        if (documentSnapshot.exists()) {
            firestore.collection(COLLECTION_NAME).document(id).delete();
            return "document with id " + id + " get deleted successfully ";
        }

        return "document with id " + id + " not found ";

    }


    public void onChange(String id) {
        System.out.println("one time");
        //  Firestore firestore = FirestoreClient.getFirestore();

        AtomicBoolean run = new AtomicBoolean(false);

        Query query = firestore.collection("users").whereEqualTo("userAddress", "shaheenbagh");
        query.addSnapshotListener(

                (snapshots, e) -> {
                    if (run.get()) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        assert snapshots != null;
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    System.out.println("added document: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    System.out.println("Modified Document: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    System.out.println("Removed Document: " + dc.getDocument().getData());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    run.set(true);
                });
    }



    public void onChange() {

        //   Firestore firestore = FirestoreClient.getFirestore();
        AtomicBoolean run = new AtomicBoolean(false);

        CollectionReference user = firestore.collection("anas");

        user.addSnapshotListener(

                (snapshots, e) -> {
                    if (run.get()) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        assert snapshots != null;
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    System.out.println("added document: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    System.out.println("Modified Document: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    System.out.println("Removed Document: " + dc.getDocument().getData());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    run.set(true);
                });

    }

    public String deleteAndRecreate() throws ExecutionException, InterruptedException {
        DocumentReference anas = firestore.collection("anas").document("123");
        ApiFuture<WriteResult> delete = anas.delete();

        Users users = new Users();
        users.setUserId(35);
        users.setUserName("ansarianas");
        users.setUserAddress("shaheenbagh");

        ApiFuture<WriteResult> set = anas.set(users);
        return set.get().getUpdateTime().toString();


    }


    public WriteBatch batchWrite() {
        DocumentReference document = firestore.collection("test").document("t1");

        WriteBatch batch = firestore.batch();

        batch.set(document, new Users());
        batch.commit();
        return batch;
    }


    public WriteBatch batchOperations() throws ExecutionException, InterruptedException {

        WriteBatch batch = firestore.batch();

        // operation 1
        DocumentReference documentReference1 = firestore.collection("test").document("t1");
        HashMap<String, Object> map = new HashMap<>();

        map.put("userId", 11);
        map.put("userName", "kiaaaaaaaa");
        map.put("userAddress", "apan address");


        batch.update(documentReference1, map);


        //operation 2

        DocumentReference documentReference2 = firestore.collection(COLLECTION_NAME).document("1");

        batch.set(documentReference2, new Users(5, "mohd", "new delhi"));


        //operation 3

        DocumentReference documentReference3 = firestore.collection("test").document("t2");

        batch.delete(documentReference3);

        ApiFuture<List<WriteResult>> listApiFuture = batch.commit();

        for (WriteResult writeResult : listApiFuture.get()) {
            System.out.println("Updated time : " + writeResult.getUpdateTime());
        }


        return batch;
    }


    public String batchTask() throws ExecutionException, InterruptedException {
        WriteBatch batch = firestore.batch();


        String mt = String.valueOf(batch.getMutationsSize());
        DocumentReference document = firestore.collection("users").document("t1");
        for (int i = 0; i < 10; i++) {
            System.out.println(batch + " 1 " + batch.getMutationsSize());
            batch.delete(document);
            Thread.sleep(1000);


            if (batch.getMutationsSize() == 10) {
                batch.commit().addListener(() -> {
                    System.out.println("commit");
                }, Executors.newCachedThreadPool());
                System.out.println(batch + " 1 " + batch.getMutationsSize());
                mt = String.valueOf(batch.getMutationsSize());
            }
        }

        return mt;

    }
}
