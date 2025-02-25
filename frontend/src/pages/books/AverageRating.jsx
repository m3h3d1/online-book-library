import React from 'react';

const AverageRating = ({ averageRating }) => {
  return (
    <div>
      <span>Average Rating: {averageRating.toFixed(2)}</span>
      <br />
      {[...Array(5)].map((star, index) => {
        const rating = index + 1;
        return (
          <span key={index}>
            {rating <= averageRating ? '★' : '☆'}
          </span>
        );
      })}
    </div>
  );
};

export default AverageRating;
