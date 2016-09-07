/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var gulp = require('gulp');
var ts = require('gulp-typescript');
var tslint = require('gulp-tslint');
var sourcemaps = require('gulp-sourcemaps');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var del = require('del');
var gulpsync = require('gulp-sync')(gulp);
var jade = require('gulp-pug');
var sass = require('gulp-sass');
var es = require('event-stream');
var autoprefixer = require('gulp-autoprefixer');
var KarmaServer = require('karma').Server;
var angularTemplateCache = require('gulp-angular-templatecache');

gulp.task('css', gulpsync.sync(['images', 'css-build', 'css-third-party', 'css-third-party-resources']));
gulp.task('images', function () {
    return gulp.src('./assets/**/*.{gif,jpg,png,svg}')
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('css-build', gulpsync.sync(['sass', 'autoprefixer', 'css-concat']));
gulp.task('css-third-party', function () {
    return gulp.src([
        './bower_components/lato/css/lato.min.css',
        './bower_components/montserrat-webfont/css/montserrat-webfont.min.css',
        './bower_components/open-sans/css/open-sans.min.css',
        './bower_components/normalize-css/normalize.css',
        './bower_components/font-awesome/css/font-awesome.min.css',
        './bower_components/codemirror/lib/codemirror.css',
        './bower_components/nvd3/build/nv.d3.min.css'
    ]).pipe(concat('_.css')).pipe(gulp.dest('../../../target/static-resources/app/third-party/styles/'));
});
gulp.task('css-third-party-resources', function () {
    var font = gulp.src([
        './bower_components/lato/font/**/*'
    ]).pipe(gulp.dest('../../../target/static-resources/app/third-party/font'));
    var fonts = gulp.src([
        './bower_components/montserrat-webfont/fonts/**/*',
        './bower_components/open-sans/fonts/**/*',
        './bower_components/font-awesome/fonts/*'
    ]).pipe(gulp.dest('../../../target/static-resources/app/third-party/fonts'));
    var foundationTemplates = gulp.src([
        './bower_components/foundation-apps/js/angular/components/**/*.html'
    ]).pipe(gulp.dest('../../../target/static-resources/components'));
    return es.concat(font, fonts, foundationTemplates);
});
gulp.task('sass', function () {
    return gulp.src('./assets/**/*.sass')
        .pipe(sass({
            outputStyle: 'compressed'
        }).on('error', sass.logError))
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('autoprefixer', function () {
    return gulp.src('../../../target/static-resources/app/styles/app.css')
        .pipe(autoprefixer({
            browsers: ['last 4 versions']
        }))
        .pipe(gulp.dest('../../../target/static-resources/app/styles/'));
});
gulp.task('css-concat', function () {
    return gulp.src(['../../../target/static-resources/app/styles/sprite.css', '../../../target/static-resources/app/styles/app.css'])
        .pipe(concat('_.css'))
        .pipe(gulp.dest('../../../target/static-resources/app/styles/'))
});

gulp.task('html', gulpsync.sync(['jade', 'html-to-js', 'copy-templates']));
gulp.task('jade', function () {
    return gulp.src('./assets/**/*.jade')
        .pipe(jade({
            locals: {}
        }))
        .pipe(gulp.dest('../../../target/static-templates/html'))
});
gulp.task('html-to-js', function () {
    return gulp.src('../../../target/static-templates/html/*/*.html')
        .pipe(angularTemplateCache({
            filename: '_templates.js',
            root: 'app/',
            module: 'tribe-main'
        }))
        .pipe(gulp.dest('../../../target/static-templates/'))
});
gulp.task('copy-templates', function () {
    return gulp.src('../../../target/static-templates/_templates.js')
        .pipe(gulp.dest('../../../target/static-resources/app/scripts/'));
});

gulp.task('js', gulpsync.sync(['compile-ts', 'copy-ts', 'js-third-party']));
gulp.task('lint-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('prose'));
});
gulp.task('compile-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(sourcemaps.init())
        .pipe(ts({
            'target': 'es5',
            'sourceMap': true,
            'out': '_.js'
        }))
        .pipe(uglify({
            mangle: false // otherwhise the sourcemap/debugger does not work properly.
        }))
        .pipe(sourcemaps.write({includeContent: false}))
        .pipe(gulp.dest('../../../target/static-resources/app/scripts/'));
});
gulp.task('copy-ts', function () {
    return gulp.src('./assets/**/*.ts')
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});

gulp.task('js-third-party', function () {
    var _1 = gulp.src([
        './bower_components/underscore/underscore-min.js',
        './bower_components/jquery/dist/jquery.min.js',
        './bower_components/js-base64/base64.min.js'
    ]).pipe(concat('_1.js')).pipe(gulp.dest('../../../target/static-resources/app/third-party/'));
    var _2 = gulp.src([
        './bower_components/angular/angular.min.js',
        './bower_components/angular-route/angular-route.min.js'
    ]).pipe(concat('_2.js')).pipe(gulp.dest('../../../target/static-resources/app/third-party/'));
    var _3 = gulp.src([
        './bower_components/foundation-apps/dist/js/foundation-apps.min.js',
        './bower_components/ngstorage/ngStorage.min.js',
        './bower_components/angular-cookies/angular-cookies.min.js',
        './bower_components/angular-resource/angular-resource.min.js',
        './bower_components/codemirror/lib/codemirror.js',
        './bower_components/angular-ui-codemirror/ui-codemirror.min.js',
        './bower_components/codemirror/mode/markdown/markdown.js',
        './bower_components/marked/lib/marked.js',
        './bower_components/angular-marked/dist/angular-marked.min.js'
    ]).pipe(concat('_3.js')).pipe(gulp.dest('../../../target/static-resources/app/third-party/'));
    // internet explorer has a file size limit of around 280kb (Yeah... no idea why)
    return es.concat(_1, _2, _3);
});

gulp.task('test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js'
    }, done).start();
});

gulp.task('copy-all', function () {
    return gulp.src(['../../../target/static-resources/**/*'])
        .pipe(gulp.dest('../../../target/apache-tomee/webapps/registry/'));
});

gulp.task('clean', function (callback) {
    return del([
        '../../../target/static-resources/',
        '../../../target/apache-tomee/webapps/registry/app/',
        '../../../target/apache-tomee/webapps/registry/components/'
    ], {
        force: true
    }, callback);
});

gulp.task('build', gulpsync.sync(['clean', 'html', 'js', 'css', 'copy-all']));
//gulp.task('build-with-tests', gulpsync.sync(['build', 'test']));

gulp.task('default', gulpsync.sync(['build']), function () {
    gulp.watch(
        ['./assets/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build'])
    );
});
