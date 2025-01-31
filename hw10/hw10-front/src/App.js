import React, { useState } from 'react';

function BookList({ goBack }) {
  const [books, setBooks] = useState([]);
  const [currentPage, setCurrentPage] = useState('list');
  const [editingBook, setEditingBook] = useState(null);

  React.useEffect(() => {
    fetch('/api/books')
      .then((response) => response.json())
      .then((data) => setBooks(data))
      .catch((error) => console.error('Error fetching books:', error));
  }, []);

  const handleDeleteBook = (id) => {
    fetch(`/api/books/${id}`, { method: 'DELETE' })
      .then(() => setBooks(books.filter((book) => book.id !== id)))
      .catch((error) => console.error('Error deleting book:', error));
  };

  if (currentPage === 'create') {
    return (
      <BookForm
        goBack={() => setCurrentPage('list')}
        onSave={(newBook) => {
          setBooks([...books, newBook]);
          setCurrentPage('list');
        }}
      />
    );
  }

  if (currentPage === 'edit') {
    return (
      <BookForm
        book={editingBook}
        goBack={() => setCurrentPage('list')}
        onSave={(updatedBook) => {
          setBooks(
            books.map((book) => (book.id === updatedBook.id ? updatedBook : book))
          );
          setCurrentPage('list');
        }}
      />
    );
  }

  return (
    <div>
      <h2>Books</h2>
      <button onClick={goBack}>Back</button>
      <button onClick={() => setCurrentPage('create')}>Create Book</button>
      <table border="1">
        <thead>
          <tr>
            <th style={{ width: '100px' }}>ID</th>
            <th style={{ width: '150px' }}>Title</th>
            <th style={{ width: '100px' }}>Edit</th>
            <th style={{ width: '100px' }}>Delete</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.id}>
              <td>{book.id}</td>
              <td>{book.title}</td>
              <td>
                <button
                  onClick={() => {
                    setEditingBook(book);
                    setCurrentPage('edit');
                  }}
                >
                  Edit
                </button>
              </td>
              <td>
                <button onClick={() => handleDeleteBook(book.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function BookForm({ book = { title: '' }, goBack, onSave }) {
  const [formState, setFormState] = useState(book);

  const handleSubmit = () => {
    const url = book.id ? `/api/books/${book.id}` : '/api/books';
    const method = book.id ? 'PUT' : 'POST';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formState),
    })
      .then((response) => response.json())
      .then((savedBook) => onSave(savedBook))
      .catch((error) => console.error('Error saving book:', error));
  };

  return (
    <div>
      <h2>{book.id ? 'Edit Book' : 'Create Book'}</h2>
      <button onClick={goBack}>Back</button>
      <div>
        <label>
          Title:
          <input
            type="text"
            value={formState.title}
            onChange={(e) => setFormState({ ...formState, title: e.target.value })}
            placeholder="Enter book title"
          />
        </label>
      </div>
      <button onClick={handleSubmit}>{book.id ? 'Update' : 'Create'}</button>
    </div>
  );
}

function AuthorsList({ goBack }) {
  const [authors, setAuthors] = useState([]);

  React.useEffect(() => {
    fetch('/api/authors')
      .then((response) => response.json())
      .then((data) => setAuthors(data))
      .catch((error) => console.error('Error fetching authors:', error));
  }, []);

  return (
    <div>
      <h2>Authors</h2>
      <button onClick={goBack}>Back</button>
      <table border="1">
        <thead>
          <tr>
            <th style={{ width: '100px' }}>ID</th>
            <th style={{ width: '150px' }}>Full Name</th>
          </tr>
        </thead>
        <tbody>
          {authors.map((author) => (
            <tr key={author.id}>
              <td>{author.id}</td>
              <td>{author.fullName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function GenresList({ goBack }) {
  const [genres, setGenres] = useState([]);

  React.useEffect(() => {
    fetch('api/genres')
      .then((response) => response.json())
      .then((data) => setGenres(data))
      .catch((error) => console.error('Error fetching authors:', error));
  }, []);

  return (
    <div>
      <h2>Genres</h2>
      <button onClick={goBack}>Back</button>
      <table border="1">
        <thead>
          <tr>
            <th style={{ width: '100px' }}>ID</th>
            <th style={{ width: '150px' }}>Name</th>
          </tr>
        </thead>
        <tbody>
          {genres.map((genre) => (
            <tr key={genre.id}>
              <td>{genre.id}</td>
              <td>{genre.name}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function App() {
  const [currentPage, setCurrentPage] = useState('main');

  const goBack = () => setCurrentPage('main');

  return (
    <div>
      {currentPage === 'main' && (
        <div>
          <h1>Main Page</h1>
          <button onClick={() => setCurrentPage('books')}>Books</button>
          <button onClick={() => setCurrentPage('authors')}>Authors</button>
          <button onClick={() => setCurrentPage('genres')}>Genres</button>
        </div>
      )}

      {currentPage === 'books' && <BookList goBack={goBack} />}
      {currentPage === 'authors' && <AuthorsList goBack={goBack} />}
      {currentPage === 'genres' && <GenresList goBack={goBack} />}
    </div>
  );
}

export default App;
