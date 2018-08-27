package com.example.rk.photoblog;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Frament extends Fragment {
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private List<User>userList;
    private FirebaseFirestore firebaseFirestore;

    private BlogRecyclerAdapter blogRecyclerAdapter;

    private FirebaseAuth firebaseAuth;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;



    public Home_Frament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__frament, container, false);

        blog_list = new ArrayList<>();
//        userList = new ArrayList<>();

        blog_list_view = view.findViewById(R.id.blogpostlist);

        firebaseAuth = FirebaseAuth.getInstance();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);

        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        blog_list_view.setAdapter(blogRecyclerAdapter);

//        if(firebaseAuth.getCurrentUser() != null) {
//
//            firebaseFirestore = FirebaseFirestore.getInstance();
//
//            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);
//
//                    if(reachedBottom){
//
//                        loadMorePost();
//
//                    }
//
//                }
//            });
//
//            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
//            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//
//                    if (!documentSnapshots.isEmpty()) {
//
//                        if (isFirstPageFirstLoad) {
//
//                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
//                            blog_list.clear();
//
//                        }
//
//                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//
//                            if (doc.getType() == DocumentChange.Type.ADDED) {
//
//                                String blogPostId = doc.getDocument().getId();
//                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
//
//                                if (isFirstPageFirstLoad) {
//
//                                    blog_list.add(blogPost);
//
//                                } else {
//
//                                    blog_list.add(0, blogPost);
//
//                                }
//
//
//                                blogRecyclerAdapter.notifyDataSetChanged();
//
//                            }
//                        }
//
//                        isFirstPageFirstLoad = false;
//
//                    }
//
//                }
//
//            });


        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            Query firstQuery = firebaseFirestore.collection("Post").orderBy("timestamp", Query.Direction.DESCENDING);

            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogpostid = doc.getDocument().getId();


                             final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogpostid);

                            String blog_user_id = doc.getDocument().getString("current_user");

//                            firebaseFirestore.collection("User").document(blog_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot snapshot = task.getResult();
//
//                                        if (snapshot.exists()) {
//
//                                            User user = task.getResult().toObject(User.class);
//                                            userList.add(user);
//
//
//
//                                        }
//
//
//
//
//
//
//
//                                    }
//
//                                }
//                            });

                            blog_list.add(blogPost);

                            blogRecyclerAdapter.notifyDataSetChanged();







                        }
                    }

                }
            });

        }

        return view;


    }



}


