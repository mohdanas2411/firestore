package com.fireStore.fireStoreDemo.service;

import com.fireStore.fireStoreDemo.entity.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private static final String COLLECTION_NAME = "users";

    public String saveUser(Users user) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = firestore.collection(COLLECTION_NAME).document(user.getUserId() + "").set(user);

        return apiFuture.get().getUpdateTime().toString();

    }

    public List<Users> getUsers() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        //get collection
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firestore.collection(COLLECTION_NAME).get();


        //document snapshots
        QuerySnapshot queryDocumentSnapshots = querySnapshotApiFuture.get();


        //list of all documents
        List<QueryDocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

        //first filter check if document is not null then it passes to map opeator
        // map documents to Users.class type
        List<Users> users = documents.stream().filter(document -> document.exists()).map(document -> document.toObject(Users.class)).collect(Collectors.toList());


        return users;

        // or we can do this in one line without storing it operation reference
        //first filter check if document is not null then it passes to map opeator
        //return firestore.collection(COLLECTION_NAME).get().get().getDocuments().stream().filter(document -> document.exists()).map(document -> document.toObject(Users.class)).collect(Collectors.toList());
    }

    public Users getUserById(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        return firestore.collection(COLLECTION_NAME).document(id).get().get().toObject(Users.class);
    }

    public String getUpdatedTime(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        Timestamp updateTime = firestore.collection(COLLECTION_NAME).document(id).get().get().getUpdateTime();


        ZonedDateTime instant = ZonedDateTime.of(updateTime.toSqlTimestamp().toLocalDateTime(), ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        return instant.format(formatter);
    }

    public String deleteUserById(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot documentSnapshot = firestore.collection(COLLECTION_NAME).document(id).get().get();

        if (documentSnapshot.exists()) {
            ApiFuture<WriteResult> delete = firestore.collection(COLLECTION_NAME).document(id).delete();
            return "document with id " + id + " get deleted successfully ";
        }

        return "document with id " + id + " not found ";

    }


    public void onChange(String id) {
        System.out.println("one time");
        Firestore firestore = FirestoreClient.getFirestore();
        String msg;
        AtomicBoolean run = new AtomicBoolean(false);

        Query query = firestore.collection("users")
                .whereEqualTo("userAddress", "shaheenbagh");
        query.addSnapshotListener(

                (snapshots, e) -> {
                    if (run.get()) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

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

        Firestore firestore = FirestoreClient.getFirestore();
        AtomicBoolean run = new AtomicBoolean(false);

        CollectionReference user = firestore.collection("users");

        user.addSnapshotListener(

                (snapshots, e) -> {
                    if (run.get()) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

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
}
