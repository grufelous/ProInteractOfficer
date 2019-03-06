const defaultUserInfo = {
  name: 'Hello Aneesh',
  image: 'https://img.icons8.com/ios/50/000000/administrator-male.png'
};

export default function reducer(state = {
  user: defaultUserInfo
}, action) {
  return state;
}