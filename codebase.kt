
// This class defines a RecyclerView Adapter that binds a list of PostView objects to a RecyclerView.
class SearchResultsPostListView(
    // A list of PostView objects (the data source for the RecyclerView).
    private val postList: List<PostView>, 

    // A base URL for external image links (used to load post images).
    private val imageExternalLink: String,

    // A lambda function that will be triggered when a post is clicked.
    private val viewPostListener: (postKey: String, postTitle: String) -> Unit
) : RecyclerView.Adapter<SearchResultsPostListView.ViewHolder>() {

    // Creates a new ViewHolder (the item view) when RecyclerView needs it.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsPostListView.ViewHolder {
        // Inflate the XML layout for each item (item_post_view.xml).
        val theView = LayoutInflater.from(parent.context).inflate(R.layout.item_post_view, parent, false)
        return ViewHolder(theView)
    }

    // Binds the data to the ViewHolder (UI components) for each item in the RecyclerView.
    override fun onBindViewHolder(holder: SearchResultsPostListView.ViewHolder, position: Int) {
        // Set up the view references (initialize variables for UI components).
        holder.setVariables()

        // Bind the data from the specific PostView at the given position to the views.
        holder.bindView(postList[position], imageExternalLink, viewPostListener)
    }

    // Returns the total number of items in the list (postList).
    override fun getItemCount(): Int = postList.size

    // ViewHolder class that represents each item view in the RecyclerView.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Declare variables for UI components that make up each item in the list.
        private lateinit var viewEntireViewContainer: ConstraintLayout
        private lateinit var viewPostContainer: CardView
        private lateinit var thumbnailImage: ImageView
        private lateinit var postTitleTxt: TextView
        private lateinit var postRatingTxt: TextView
        private lateinit var postCategoryTxt: TextView

        // Function to initialize the UI components by finding their view IDs in the item layout.
        fun setVariables() {
            viewEntireViewContainer = itemView.findViewById(R.id.viewEntireViewContainer)
            viewPostContainer = itemView.findViewById(R.id.viewPostContainer)
            thumbnailImage = itemView.findViewById(R.id.thumbnailImage)
            postTitleTxt = itemView.findViewById(R.id.postTitleTxt)
            postRatingTxt = itemView.findViewById(R.id.post_rating_txt)
            postCategoryTxt = itemView.findViewById(R.id.post_category_txt)
        }

        // Function to bind the data from a PostView object to the UI components.
        fun bindView(
            postView: PostView,              // The PostView object that contains the data to bind.
            imageExternalLink: String,       // The base URL for loading images.
            viewPostListener: (postKey: String, postTitle: String) -> Unit // Lambda to handle clicks on the post.
        ) {
            // Concatenate the base image URL with the specific post's image link.
            val imageLink = imageExternalLink + postView.imageLink

            // Set content description for accessibility for the thumbnail image.
            thumbnailImage.contentDescription = itemView.context.getString(R.string.app_singleString, postView.theTitle)

            // Set the text for the post title, rating, and category.
            postTitleTxt.text = itemView.context.getString(R.string.app_singleString, postView.theTitle)
            postRatingTxt.text = itemView.context.getString(R.string.app_singleString, postView.currentPostRating)
            postCategoryTxt.text = itemView.context.getString(R.string.app_singleString, postView.contentType)

            // Set a click listener on the container for the entire view (though it's currently empty).
            viewEntireViewContainer.setOnClickListener { }

            // Use Glide (an image loading library) to load the image from the given URL into the ImageView.
            Glide.with(itemView.context)
                .load(imageLink)                        // Load the image from the URL.
                .centerCrop()                           // Crop the image to fit the ImageView.
                .skipMemoryCache(true)                  // Don't cache the image in memory.
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Don't cache the image on disk.
                .placeholder(R.drawable.placeholderimage) // Show a placeholder while the image is loading.
                .error(R.drawable.placeholderimage)     // Show a placeholder if the image fails to load.
                .into(thumbnailImage)                   // Load the image into the ImageView (thumbnailImage).

            // Set a click listener on the post container to invoke the lambda function with the post's key and title.
            viewPostContainer.setOnClickListener {
                viewPostListener.invoke(postView.postKey, postView.theTitle)
            }
        }
    } 
}
