import React, { useEffect, useState } from 'react'
import axios from 'axios'
import "../css/Dashboard.css"

const Dashboard = () => {
  const [friendsPosts, setFriendsPosts] = useState([])
  const [users, setUsers] = useState([])
  const [comments, setComments] = useState({})
  const [newComments, setNewComments] = useState({})
  const [newPostContent, setNewPostContent] = useState('')
  const [searchQuery, setSearchQuery] = useState('')

  const currentUser = {
    userId: sessionStorage.getItem("userId"),
    firstName: sessionStorage.getItem("firstName"),
    lastName: sessionStorage.getItem("lastName")
  }

  useEffect(() => {
    const fetchPostsAndUsers = async () => {
      try {
        const postsResponse = await axios.get('http://localhost:8080/api/posts')
        setFriendsPosts(postsResponse.data)

        const usersResponse = await axios.get(`http://localhost:8080/api/users/getOther/${currentUser.userId}`)
        setUsers(usersResponse.data)

        const commentsResponse = await axios.get('http://localhost:8080/api/comments')
        const groupedComments = commentsResponse.data.reduce((acc, comment) => {
          const postId = comment.post.postId
          if (!acc[postId]) {
            acc[postId] = []
          }
          acc[postId].push(comment)
          return acc
        }, {})
        setComments(groupedComments)
      } catch (error) {
        console.error(error)
      }
    }

    fetchPostsAndUsers()
  }, [currentUser.userId])

  const handleAddFriend = async friendId => {
    try {
      const response = await axios.post('http://localhost:8080/api/requests', {
        sender: { userId: currentUser.userId },
        receiver: { userId: friendId }
      })

      setUsers(prev => prev.filter(user => user.userId !== friendId))
      console.log(response)
    } catch (error) {
      console.error(error)
    }
  }

  const handleCommentChange = (postId, comment) => {
    setNewComments({ ...newComments, [postId]: comment })
  }

  const handleSendComment = async postId => {
    try {
      const response = await axios.post('http://localhost:8080/api/comments', {
        post: { postId },
        user: { userId: currentUser.userId },
        content: newComments[postId]
      })
      console.log(response)

      const newComment = response.data
      newComment.user = {
        userId: currentUser.userId,
        firstName: currentUser.firstName,
        lastName: currentUser.lastName
      }

      setComments(prevComments => ({
        ...prevComments,
        [postId]: [...(prevComments[postId] || []), newComment]
      }))

      setNewComments({ ...newComments, [postId]: '' })
    } catch (error) {
      console.error(error)
    }
  }

  const handleNewPostChange = e => {
    setNewPostContent(e.target.value)
  }

  const handleSendPost = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/posts', {
        user: { userId: currentUser.userId },
        content: newPostContent
      })
      console.log(response)

      setFriendsPosts(prevPosts => [response.data, ...prevPosts])
      setNewPostContent('')
    } catch (error) {
      console.error(error)
    }
  }

  const handleSearchChange = e => {
    setSearchQuery(e.target.value)
  }

  const filteredUsers = users.filter(user =>
    user.firstName.toLowerCase().includes(searchQuery.toLowerCase()) ||
    user.lastName.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="dashboard-container">
      <h1>Home</h1>
      <div className="create-post">
        <h2>Create a Post</h2>
        <input
          type="text"
          placeholder="What's on your mind?"
          value={newPostContent}
          onChange={handleNewPostChange}
        />
        <button className="post-button" onClick={handleSendPost}>
          Post
        </button>
      </div>
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search for people..."
          value={searchQuery}
          onChange={handleSearchChange}
        />
      </div>
      <div className="display-section">
        <div className="display-content">
          <h2>Friends' Posts</h2>
          {friendsPosts.map(post => (
            <div className="posts" key={post.postId}>
              <div className="post-content">
                <h3>{post.author}</h3>
                <p>{post.content}</p>
                {comments[post.postId] &&
                  comments[post.postId].map(comment => (
                    <div className="comments" key={comment.commentId}>
                      <div className="comment">
                        <p>
                          {comment.user.firstName} {comment.user.lastName}:{' '}
                          {comment.content}
                        </p>
                      </div>
                    </div>
                  ))}
              </div>
              <div className="comment-action">
                <input
                  type="text"
                  placeholder="Add a comment"
                  value={newComments[post.postId] || ''}
                  onChange={e =>
                    handleCommentChange(post.postId, e.target.value)
                  }
                />
                <button onClick={() => handleSendComment(post.postId)}>
                  Send
                </button>
              </div>
            </div>
          ))}
        </div>
        <div className="recomment-friends">
          <h2>{searchQuery ? 'Search Results' : 'Recommended Friends'}</h2>
          {filteredUsers.map(user => (
            <div className="friends" key={user.userId}>
              <div className="friend">
                <h3>{user.firstName} {user.lastName}</h3>
                <p>{user.email}</p>
              </div>
              <div className="add-friend">
                <button onClick={() => handleAddFriend(user.userId)}>
                  Add Friend
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default Dashboard
