module.exports = {
  preset: 'jest-preset-angular',
//  globalSetup: 'jest-preset-angular/global-setup',
  transform: {
    '^.+\\.(ts|js|html)$': 'jest-preset-angular',
  },
  transformIgnorePatterns: ['node_modules/(?!@angular|jspdf)'],
  setupFiles: ['<rootDir>/jest.setup.js'],
  moduleNameMapper: {
     '^src/(.*)$': '<rootDir>/src/$1',
     '\\.(html|css)$': '<rootDir>/__mocks__/fileMock.js',
  },
  testPathIgnorePatterns: ['<rootDir>/dist/'],
};